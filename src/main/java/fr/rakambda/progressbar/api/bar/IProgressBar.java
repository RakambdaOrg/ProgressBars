package fr.rakambda.progressbar.api.bar;

import fr.rakambda.progressbar.api.render.IRenderer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.function.LongBinaryOperator;

/**
 * Defines the core elements of a progress bar.
 */
public interface IProgressBar{
	/**
	 * Gets the renderer instance associated to this bar.
	 *
	 * @return The renderer.
	 */
	@NonNull
	IRenderer getRenderer();
	
	/**
	 * Gets the name associated to this bar.
	 *
	 * @return The name.
	 */
	@Nullable
	String getName();
	
	/**
	 * Sets the name associated to this bar.
	 *
	 * @param value The name.
	 */
	void setName(@Nullable String value);
	
	/**
	 * Gets the start value associated to this bar.
	 *
	 * @return The start value.
	 */
	long getStart();
	
	/**
	 * Sets the start value associated to this bar.
	 *
	 * @param value The start value.
	 */
	void setStart(long value);
	
	/**
	 * Gets the current value associated to this bar.
	 *
	 * @return The current value.
	 */
	long getCurrent();
	
	/**
	 * Sets the current value associated to this bar.
	 *
	 * @param value The current value.
	 */
	void setCurrent(long value);
	
	/**
	 * Accumulates a value to the current value.
	 *
	 * @param value    The value to accumulate
	 * @param operator The operation to apply between the current value and the value to accumulate
	 */
	void accumulate(long value, @NonNull LongBinaryOperator operator);
	
	/**
	 * Gets the end value associated to this bar.
	 *
	 * @return The end value.
	 */
	long getEnd();
	
	/**
	 * Sets the end value associated to this bar.
	 *
	 * @param value The end value.
	 */
	void setEnd(long value);
	
	/**
	 * Increments the current value by 1.
	 */
	default void increment(){
		increment(1);
	}
	
	/**
	 * Increments the current value by {@code amount}.
	 *
	 * @param amount The amount to increment
	 */
	default void increment(int amount){
		increment((long) amount);
	}
	
	/**
	 * Increments the current value by {@code amount}.
	 *
	 * @param amount The amount to increment
	 */
	default void increment(long amount){
		accumulate(amount, Long::sum);
	}
	
	/**
	 * Indicates if the bar is considered finished or not.
	 *
	 * @return true if {@code current >= end}, otherwise false
	 */
	default boolean isFinished(){
		return getCurrent() >= getEnd();
	}
	
	/**
	 * Gets whether the bar should be hidden when finished.
	 *
	 * @return true if bar should be hidden when finished, false otherwise.
	 */
	boolean isHideWhenComplete();
	
	/**
	 * Sets whether the bar should be hidden when finished.
	 *
	 * @param value Whether to hide the bar when finished or not.
	 */
	void setHideWhenComplete(boolean value);
	
	/**
	 * Gets whether the bar should be removed when finished.
	 *
	 * @return true if bar should be removed when finished, false otherwise.
	 */
	boolean isRemoveWhenComplete();
	
	/**
	 * Sets whether the bar should be removed when finished.
	 *
	 * @param value Whether to remove the bar when finished or not.
	 */
	void setRemoveWhenComplete(boolean value);
	
	/**
	 * Gets whether the bar should display progress percentage.
	 *
	 * @return true if percentage should be displayed, false otherwise.
	 */
	boolean isShowPercentage();
	
	/**
	 * Sets whether the bar should display progress percentage.
	 *
	 * @param value Whether to display percentage or not.
	 */
	void setShowPercentage(boolean value);
	
	/**
	 * Gets the unit associated to this bar.
	 *
	 * @return The unit.
	 */
	@Nullable String getUnit();
	
	/**
	 * Sets the unit associated to this bar.
	 *
	 * @param value The unit.
	 */
	void setUnit(String value);
	
	/**
	 * Gets the unit factor associated to this bar.
	 * For example, if the bar has a value of 100 and a unit factor of 20, then 5 will be displayed.
	 *
	 * @return The unit factor.
	 */
	long getUnitFactor();
	
	/**
	 * Sets the unit factor associated to this bar.
	 * For example, if the bar has a value of 100 and a unit factor of 20, then 5 will be displayed.
	 *
	 * @param value The unit factor.
	 */
	void setUnitFactor(long value);
	
	/**
	 * Gets the description associated to this bar.
	 *
	 * @return The description.
	 */
	@Nullable String getDescription();
	
	/**
	 * Sets the description associated to this bar.
	 *
	 * @param value The description.
	 */
	void setDescription(String value);
}
