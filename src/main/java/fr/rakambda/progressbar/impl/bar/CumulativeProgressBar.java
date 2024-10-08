package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.api.bar.IComposedProgressBar;
import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.render.IRenderer;
import lombok.Builder;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.LongBinaryOperator;

/**
 * Progress bar where the start/current/end values are the sum of the children's.
 */
@ToString
public class CumulativeProgressBar extends BaseProgressBar implements IComposedProgressBar{
	@ToString.Exclude
	private Collection<IProgressBar> children;
	
	/**
	 * Builds a new instance of this progress bar.
	 *
	 * @param renderer           The renderer to use, see {@link BaseProgressBar} for default values
	 * @param name               The name
	 * @param hideWhenComplete   Whether to hide the progress bar when finished
	 * @param removeWhenComplete Whether to remove the progress bar when finished
	 * @param showPercentage     Whether to display progress percentage or not
	 * @param unit               The unit to display
	 * @param description        The description
	 */
	@Builder
	private CumulativeProgressBar(
			@Nullable IRenderer renderer,
			@Nullable String name,
			boolean hideWhenComplete,
			boolean removeWhenComplete,
			boolean showPercentage,
			@Nullable String unit,
			@Nullable Long unitFactor,
			@Nullable String description
	){
		super(renderer, name, hideWhenComplete, removeWhenComplete, showPercentage, unit, unitFactor, description);
		
		this.children = new ConcurrentLinkedQueue<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getStart(){
		return children.stream()
				.mapToLong(IProgressBar::getStart)
				.sum();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCurrent(){
		return children.stream()
				.mapToLong(IProgressBar::getCurrent)
				.sum();
	}
	
	/**
	 * @throws UnsupportedOperationException Not supported for this type of bar
	 */
	@Override
	public void setStart(long value) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @throws UnsupportedOperationException Not supported for this type of bar
	 */
	@Override
	public void setCurrent(long value) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @throws UnsupportedOperationException Not supported for this type of bar
	 */
	@Override
	public void accumulate(long value, @NotNull LongBinaryOperator operator){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @throws UnsupportedOperationException Not supported for this type of bar
	 */
	@Override
	public void setEnd(long value) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getEnd(){
		return children.stream()
				.mapToLong(IProgressBar::getEnd)
				.sum();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@NotNull
	public Collection<IProgressBar> getChildren(){
		return children;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public <T extends IProgressBar> T addProgressBar(@NotNull T progressBar){
		children.add(progressBar);
		return progressBar;
	}
}
