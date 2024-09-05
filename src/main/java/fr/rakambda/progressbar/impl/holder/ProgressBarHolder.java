package fr.rakambda.progressbar.impl.holder;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.update.IProgressBarTask;
import fr.rakambda.progressbar.impl.update.DefaultProgressBarTask;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is the base of all the interactions with the progress bars.
 * Bars will be added into this container with {@link #addProgressBar(IProgressBar)} in order for them to be displayed.
 */
public class ProgressBarHolder implements AutoCloseable{
	@NotNull
	private final ScheduledExecutorService executorService;
	@NotNull
	private final IProgressBarTask<IProgressBar> progressBarTask;
	@NotNull
	private final ScheduledFuture<?> progressBarTaskFuture;
	
	/**
	 * Builds a new holder.
	 *
	 * @param executorService The executor service that will schedule the update tasks. Defaults to an instance of a threaded pool with 1 thread (see {@link  #buildDefaultExecutor()}).
	 * @param progressBarTask The taskl to use to update progress bars. Defaults to an instance of {@link DefaultProgressBarTask} printing its result on stderr.
	 * @param refreshRate     The refresh rate of the bars in milliseconds. Default to 1000.
	 */
	@Builder
	private ProgressBarHolder(
			@Nullable ScheduledExecutorService executorService,
			@Nullable IProgressBarTask<IProgressBar> progressBarTask,
			@Nullable Long refreshRate
	){
		executorService = Optional.ofNullable(executorService).orElseGet(this::buildDefaultExecutor);
		progressBarTask = Optional.ofNullable(progressBarTask).orElse(new DefaultProgressBarTask(new PrintStream(new FileOutputStream(FileDescriptor.err))));
		refreshRate = Optional.ofNullable(refreshRate).orElse(1000L);
		
		this.executorService = executorService;
		this.progressBarTask = progressBarTask;
		this.progressBarTaskFuture = executorService.scheduleWithFixedDelay(progressBarTask, 0, refreshRate, TimeUnit.MILLISECONDS);
	}
	
	@NotNull
	private ScheduledExecutorService buildDefaultExecutor(){
		return Executors.newScheduledThreadPool(1, runnable -> {
			var thread = Executors.defaultThreadFactory().newThread(runnable);
			thread.setName("ProgressBar");
			thread.setDaemon(true);
			return thread;
		});
	}
	
	/**
	 * Adds a bar to this holder.
	 *
	 * @param progressBar The bar to add
	 */
	@NotNull
	public <V extends IProgressBar> V addProgressBar(@NotNull V progressBar){
		this.progressBarTask.addProgressBar(progressBar);
		return progressBar;
	}
	
	/**
	 * Removes a bar to this holder.
	 *
	 * @param progressBar The bar to remove
	 */
	public void removeProgressBar(@NotNull IProgressBar progressBar){
		this.progressBarTask.removeProgressBar(progressBar);
	}
	
	/**
	 * Terminates all progress bars and shutdown the provided {@link #executorService}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws Exception{
		this.progressBarTaskFuture.cancel(true);
		this.executorService.shutdown();
		this.progressBarTask.close();
	}
}
