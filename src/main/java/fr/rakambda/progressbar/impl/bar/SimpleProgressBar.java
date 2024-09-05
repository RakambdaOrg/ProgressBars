package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.render.IRenderer;
import lombok.Builder;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple progress bar where values are updated manually.
 * It consists in a start value, current value, and end value.
 *
 * <pre>{@code
 * |oooooooooo|xxxxxxxxx|
 * start    current    end
 * }</pre>
 *
 * <ul>
 *     <li>start: the starting point of the progress bar. For example if the base is at 10 and current at 40, then the progress bar would have progressed by 30 units.</li>
 *     <li>current: the current value of the bar</li>
 *     <li>end: the target value the bar should get to in order to consider the bar finished</li>
 * </ul>
 */
@ToString
public class SimpleProgressBar extends BaseProgressBar implements IProgressBar{
	@NotNull
	private AtomicLong start;
	@NotNull
	private AtomicLong current;
	@NotNull
	private AtomicLong end;
	
	/**
	 * Builds a new instance of this progress bar.
	 *
	 * @param renderer           The renderer to use, see {@link BaseProgressBar} for default values
	 * @param name               The name
	 * @param start              The start value, must be positive
	 * @param current            The current value, must be greater than or equal to start
	 * @param end                The end value, must be greater than or equal to current
	 * @param hideWhenComplete   Whether to hide the progress bar when finished
	 * @param removeWhenComplete Whether to remove the progress bar when finished
	 * @param showPercentage     Whether to display progress percentage or not
	 * @param unit               The unit to display
	 * @param description        The description
	 */
	@Builder
	private SimpleProgressBar(
			@Nullable IRenderer renderer,
			@Nullable String name,
			@Nullable AtomicLong start,
			@Nullable AtomicLong current,
			@Nullable AtomicLong end,
			boolean hideWhenComplete,
			boolean removeWhenComplete,
			boolean showPercentage,
			@Nullable String unit,
			@Nullable String description
	){
		super(renderer, name, hideWhenComplete, removeWhenComplete, showPercentage, unit, description);
		
		start = Optional.ofNullable(start).orElse(new AtomicLong());
		current = Optional.ofNullable(current).orElse(new AtomicLong());
		end = Optional.ofNullable(end).orElse(new AtomicLong());
		
		if(current.get() < 0){
			throw new IllegalArgumentException("Start value must be positive");
		}
		if(current.get() < start.get()){
			throw new IllegalStateException("Current value must be greater than or equal to the start value");
		}
		if(end.get() < current.get()){
			throw new IllegalStateException("End value must be greater than or equal to the current value");
		}
		this.start = start;
		this.current = current;
		this.end = end;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getStart(){
		return start.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStart(long value){
		if(value < 0){
			throw new IllegalArgumentException("Start value must be positive");
		}
		if(value > getEnd()){
			throw new IllegalArgumentException("Start value must be lower than or equal to the end value");
		}
		start.set(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCurrent(){
		return current.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCurrent(long value){
		if(value < 0){
			throw new IllegalArgumentException("Current value must be positive");
		}
		current.set(value);
		if(value > getEnd()){
			setEnd(value);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getEnd(){
		return end.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnd(long value){
		if(value < 0){
			throw new IllegalArgumentException("End value must be positive");
		}
		if(value < getStart()){
			throw new IllegalArgumentException("End value must be greater than or equal to the start value");
		}
		end.set(value);
	}
}
