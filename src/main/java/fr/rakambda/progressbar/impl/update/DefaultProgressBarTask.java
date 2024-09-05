package fr.rakambda.progressbar.impl.update;

import fr.rakambda.progressbar.api.bar.IComposedProgressBar;
import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.update.IProgressBarTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Default task supporting all progress bars.
 * {@link IComposedProgressBar} will be indented based on their depth.
 * The width will be inferred from the terminal, with a minimum of {@value #MIN_WIDTH}.
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultProgressBarTask implements IProgressBarTask<IProgressBar>{
	private static final int MIN_WIDTH = 30;
	private static final char CARRIAGE_RETURN = '\r';
	private static final char NEW_LINE = '\n';
	private static final char ESCAPE_CHAR = '\u001b';
	
	@NotNull
	private final PrintStream out;
	
	@NotNull
	private final Queue<IProgressBar> progressBars = new ConcurrentLinkedQueue<>();
	
	private long previousLineCount = 0;
	@Nullable
	private Terminal terminal;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(){
		removeCompletedBars(progressBars);
		
		var terminalWidth = Optional.ofNullable(getTerminal()).map(Terminal::getWidth).orElse(0);
		var maxLineWidth = Math.max(MIN_WIDTH, terminalWidth);
		
		var stringBuilder = new StringBuilder();
		printBars(stringBuilder, 0, maxLineWidth, progressBars);
		
		var lineCount = stringBuilder.chars().filter(c -> c == NEW_LINE).count();
		if(previousLineCount > lineCount){
			// If we have less lines than before, erase the extra ones.
			var diff = previousLineCount - lineCount;
			out.print(cursorUp(previousLineCount));
			for(int i = 0; i < diff; i++){
				out.print(eraseLine());
				out.print(NEW_LINE);
			}
		}
		else if(previousLineCount > 0){
			// If we have more, or the same line, just go back to where we were before.
			// Extra lines will be added at the end.
			out.print(cursorUp(previousLineCount));
		}
		
		out.print(stringBuilder);
		out.flush();
		
		previousLineCount = lineCount;
	}
	
	/**
	 * Counts the number of bars that are not hidden among this list.
	 *
	 * @param progressBars The bars to check.
	 *
	 * @return The number of non-hidden bars
	 */
	private int countNonHiddenBars(@NotNull Collection<IProgressBar> progressBars){
		return progressBars.stream().mapToInt(this::countNonHiddenBars).sum();
	}
	
	/**
	 * Counts the number of bars that are not hidden including itself and its children.
	 *
	 * @param progressBar The bar to check.
	 *
	 * @return The number of non-hidden bars
	 */
	private int countNonHiddenBars(@NotNull IProgressBar progressBar){
		var count = progressBar.isHideWhenComplete() && progressBar.isFinished() ? 0 : 1;
		if(progressBar instanceof IComposedProgressBar composedProgressBar){
			count += countNonHiddenBars(composedProgressBar.getChildren());
		}
		return count;
	}
	
	/**
	 * Removes completed bars from the list if they should be.
	 *
	 * @param progressBars The bars to potentially remove.
	 */
	private void removeCompletedBars(@NotNull Collection<IProgressBar> progressBars){
		progressBars.removeIf(this::removeCompletedBar);
	}
	
	/**
	 * Removes this progress bar if completed and {@link IProgressBar#isRemoveWhenComplete()} is true.
	 * This also iterates over the children.
	 */
	private boolean removeCompletedBar(@NotNull IProgressBar progressBar){
		if(progressBar instanceof IComposedProgressBar composedProgressBar){
			removeCompletedBars(composedProgressBar.getChildren());
		}
		return progressBar.isRemoveWhenComplete() && progressBar.getCurrent() >= progressBar.getEnd();
	}
	
	/**
	 * Prints a lists of bars.
	 *
	 * @param stringBuilder The output where the bars are written to
	 * @param currentDepth  Teh current depth, used to make the indentation
	 * @param maxLineWidth  The maximum width of the line
	 * @param progressBars  The bars to print
	 */
	private void printBars(@NotNull StringBuilder stringBuilder, int currentDepth, int maxLineWidth, @NotNull Collection<IProgressBar> progressBars){
		var iterator = progressBars.iterator();
		
		while(iterator.hasNext()){
			var progressBar = iterator.next();
			
			if(!progressBar.isHideWhenComplete() || !progressBar.isFinished()){
				stringBuilder.append(CARRIAGE_RETURN);
				stringBuilder.repeat("┃ ", Math.max(0, currentDepth - 1));
				if(currentDepth > 0){
					if(iterator.hasNext()){
						stringBuilder.append("┣━");
					}
					else{
						stringBuilder.append("┗━");
					}
				}
				stringBuilder.append(progressBar.getRenderer().render(maxLineWidth - (2 * currentDepth), progressBar));
				stringBuilder.append(NEW_LINE);
			}
			
			if(progressBar instanceof IComposedProgressBar composedProgressBar){
				printBars(stringBuilder, currentDepth + 1, maxLineWidth, composedProgressBar.getChildren());
			}
		}
	}
	
	/**
	 * @return String sequence to clear the whole terminal
	 */
	@NotNull
	private String clearConsole(){
		return ESCAPE_CHAR + "[2J" + ESCAPE_CHAR + "[H" + CARRIAGE_RETURN;
	}
	
	/**
	 * @return String sequence to clear the current line
	 */
	@NotNull
	private String eraseLine(){
		return ESCAPE_CHAR + "[2K";
	}
	
	/**
	 * @param upCount The numbers of lines to go up
	 *
	 * @return String sequence to move the cursor up {@code upCount} lines
	 */
	@NotNull
	private String cursorUp(long upCount){
		return ESCAPE_CHAR + "[" + upCount + "F";
	}
	
	/**
	 * Adds a bar to this holder.
	 *
	 * @param progressBar The bar to add
	 */
	public void addProgressBar(@NotNull IProgressBar progressBar){
		progressBars.add(progressBar);
	}
	
	/**
	 * Removes a bar to this holder.
	 *
	 * @param progressBar The bar to remove
	 */
	public void removeProgressBar(@NotNull IProgressBar progressBar){
		progressBars.remove(progressBar);
	}
	
	/**
	 * Gets the current terminal.
	 *
	 * @return The terminal, or null if it failed to get it
	 */
	@Nullable
	private Terminal getTerminal(){
		if(Objects.isNull(terminal)){
			try{
				this.terminal = TerminalBuilder.builder().dumb(true).build();
			}
			catch(IOException e){
				log.warn("Failed to get terminal info, this should not be possible", e);
			}
		}
		return this.terminal;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IOException If the terminal info holder cannot be closed.
	 */
	@Override
	public void close() throws IOException{
		if(Objects.nonNull(terminal)){
			terminal.close();
		}
	}
}
