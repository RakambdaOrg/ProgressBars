package fr.rakambda.progressbar.impl.render;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.render.IRenderer;
import org.jspecify.annotations.NonNull;
import java.util.Objects;

/**
 * Displays basic info.
 * The layout is the following :
 * <pre>{@code
 * <name> <percentage> <progress> <count><unit> <description>
 * }</pre>
 *
 * <ul>
 *     <li>name: Optional, displays the bar's name</li>
 *     <li>percentage: Optional, displays in the format xx% the progress of the bar</li>
 *     <li>progress: the bar itself filling up as it progresses. Empty bar is represented by {@value #INCOMPLETE} and filled with {@value #COMPLETE}</li>
 *     <li>count: the values of the progress bar under the format {@code <current>/<max>}</li>
 *     <li>unit: Optional, the unit of the count</li>
 *     <li>description: Optional, the bar's description</li>
 * </ul>
 */
public class DefaultRenderer implements IRenderer{
	private static final char INCOMPLETE = '░';
	private static final char COMPLETE = '█';
	
	/**
	 * {@inheritDoc}
	 */
	@NonNull
	@Override
	public String render(int maxLength, @NonNull IProgressBar bar){
		var prefix = new StringBuilder(maxLength);
		var middlePart = new StringBuilder(maxLength);
		var suffix = new StringBuilder(maxLength);
		
		var start = bar.getStart();
		var end = bar.getEnd();
		
		var elapsed = bar.getCurrent() - start;
		var totalSteps = end - start;
		
		var normalizedElapsed = Math.min(totalSteps, Math.max(0, elapsed));
		var progress = totalSteps == 0 ? 0 : Math.min(1, ((float) normalizedElapsed) / totalSteps);
		
		if(Objects.nonNull(bar.getName())){
			prefix.append(bar.getName());
			prefix.append(' ');
		}
		if(bar.isShowPercentage()){
			prefix.append("%3d%%".formatted((int) (progress * 100)));
			prefix.append(' ');
		}
		
		suffix.append(' ');
		suffix.append(elapsed / bar.getUnitFactor());
		suffix.append('/');
		suffix.append(totalSteps / bar.getUnitFactor());
		if(Objects.nonNull(bar.getUnit())){
			suffix.append(bar.getUnit());
		}
		if(Objects.nonNull(bar.getDescription())){
			suffix.append(' ');
			suffix.append(bar.getDescription());
		}
		
		var barLength = maxLength - prefix.length() - suffix.length();
		printBar(middlePart, totalSteps, progress, barLength);
		
		return prefix.toString() + middlePart + suffix;
	}
	
	/**
	 * Writes the progress part of the bar.
	 *
	 * @param out        Where to write the progress
	 * @param totalSteps The number of steps the bar have to take
	 * @param progress   The percentage (from 0 to 1) the bar has progressed
	 * @param barLength  The bar's length
	 */
	private void printBar(@NonNull StringBuilder out, long totalSteps, float progress, int barLength){
		if(barLength <= 0){
			return;
		}
		
		if(totalSteps == 0){
			out.repeat('-', barLength);
			return;
		}
		
		var completeLength = (int) (progress * barLength);
		var incompleteLength = barLength - completeLength;
		
		out.repeat(COMPLETE, completeLength);
		out.repeat(INCOMPLETE, incompleteLength);
	}
}
