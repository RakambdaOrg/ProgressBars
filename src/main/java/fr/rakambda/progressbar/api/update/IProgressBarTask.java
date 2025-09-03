package fr.rakambda.progressbar.api.update;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import org.jspecify.annotations.NonNull;

/**
 * This task is responsible for displaying a group of progress bars.
 * It does not care about the render itself of the bar that is handled by {@link fr.rakambda.progressbar.api.render.IRenderer} but performs the general tasks of moving the cursor into the right position (including indentation), clear lines, indent bars, etc.
 */
public interface IProgressBarTask<T extends IProgressBar> extends Runnable, AutoCloseable{
	/**
	 * Adds a bar to this holder.
	 *
	 * @param progressBar The bar to add
	 */
	void addProgressBar(@NonNull T progressBar);
	
	/**
	 * Removes a bar to this holder.
	 *
	 * @param progressBar The bar to remove
	 */
	void removeProgressBar(@NonNull T progressBar);
}
