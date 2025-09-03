package fr.rakambda.progressbar.api.bar;

import org.jspecify.annotations.NonNull;
import java.util.Collection;

/**
 * Defines elements that compose a progress bar with children.
 */
public interface IComposedProgressBar extends IProgressBar{
	/**
	 * Gets the children of this bar.
	 *
	 * @return The children
	 */
	@NonNull
	Collection<IProgressBar> getChildren();
	
	@NonNull
	<V extends IProgressBar> V addProgressBar(@NonNull V progressBar);
}
