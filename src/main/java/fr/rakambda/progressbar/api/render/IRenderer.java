package fr.rakambda.progressbar.api.render;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import org.jspecify.annotations.NonNull;

/**
 * Renders a progress bar, not minding about indentation and other things of that kind.
 * It solely focuses on displaying infos of that bar regardless of its position on the screen.
 */
public interface IRenderer{
	/**
	 * Renders a bar.
	 *
	 * @param maxLength The maximum length the line should have
	 * @param bar       The bar to render
	 *
	 * @return The bar's render
	 */
	@NonNull
	String render(int maxLength, @NonNull IProgressBar bar);
}
