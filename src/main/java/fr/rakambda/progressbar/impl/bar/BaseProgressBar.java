package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.api.render.IRenderer;
import fr.rakambda.progressbar.impl.render.DefaultRenderer;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Optional;

/**
 * Ease of use class containing all the "metadata" fields of a {@link fr.rakambda.progressbar.api.bar.IProgressBar}.
 */
@Getter
abstract class BaseProgressBar{
	@NonNull
	private IRenderer renderer;
	@Nullable
	@Setter
	private String name;
	@Setter
	private boolean hideWhenComplete;
	@Setter
	private boolean removeWhenComplete;
	@Setter
	private boolean showPercentage;
	@Nullable
	@Setter
	private String unit;
	@Setter
	@Getter
	private long unitFactor;
	@Nullable
	@Setter
	private String description;
	
	public BaseProgressBar(
			@Nullable IRenderer renderer,
			@Nullable String name,
			boolean hideWhenComplete,
			boolean removeWhenComplete,
			boolean showPercentage,
			@Nullable String unit,
			@Nullable Long unitFactor,
			@Nullable String description
	){
		this.renderer = Optional.ofNullable(renderer).orElse(new DefaultRenderer());
		this.name = name;
		this.hideWhenComplete = hideWhenComplete;
		this.removeWhenComplete = removeWhenComplete;
		this.showPercentage = showPercentage;
		this.unit = unit;
		this.unitFactor = Optional.ofNullable(unitFactor).filter(v -> v != 0L).orElse(1L);
		this.description = description;
	}
}
