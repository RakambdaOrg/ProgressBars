package fr.rakambda.progressbar.api.bar;

import org.jetbrains.annotations.NotNull;
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
	@NotNull
	Collection<IProgressBar> getChildren();
}
