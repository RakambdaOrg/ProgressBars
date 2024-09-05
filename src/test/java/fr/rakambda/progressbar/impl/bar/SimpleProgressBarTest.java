package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.impl.ParallelizableTest;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ParallelizableTest
class SimpleProgressBarTest extends BaseProgressBarTest<SimpleProgressBar>{
	@Override
	protected SimpleProgressBar getProgressBar(){
		return SimpleProgressBar.builder().build();
	}
	
	@Test
	void defaultValues(){
		var tested = getProgressBar();
		assertThat(tested.getStart()).isEqualTo(0);
		assertThat(tested.getCurrent()).isEqualTo(0);
		assertThat(tested.getEnd()).isEqualTo(0);
	}
	
	@Test
	void customValues(){
		var tested = SimpleProgressBar.builder()
				.start(new AtomicLong(10))
				.current(new AtomicLong(10))
				.end(new AtomicLong(10))
				.build();
		
		assertThat(tested.getStart()).isEqualTo(10);
		assertThat(tested.getCurrent()).isEqualTo(10);
		assertThat(tested.getEnd()).isEqualTo(10);
	}
	
	@Test
	void setProgressValues(){
		var tested = getProgressBar();
		tested.setEnd(100);
		tested.setStart(10);
		tested.setCurrent(50);
		
		assertThat(tested.getStart()).isEqualTo(10);
		assertThat(tested.getCurrent()).isEqualTo(50);
		assertThat(tested.getEnd()).isEqualTo(100);
	}
	
	@Test
	void increment(){
		var tested = getProgressBar();
		tested.setEnd(100);
		tested.setStart(10);
		tested.setCurrent(50);
		
		tested.increment();
		
		assertThat(tested.getCurrent()).isEqualTo(51);
	}
	
	@Test
	void incrementCustom(){
		var tested = getProgressBar();
		tested.setEnd(100);
		tested.setStart(10);
		tested.setCurrent(50);
		
		tested.increment(5);
		
		assertThat(tested.getCurrent()).isEqualTo(55);
	}
	
	@Test
	void settingCurrentAfterEndModifiesEnd(){
		var tested = getProgressBar();
		tested.setEnd(50);
		tested.setStart(10);
		tested.setCurrent(100);
		
		assertThat(tested.getStart()).isEqualTo(10);
		assertThat(tested.getCurrent()).isEqualTo(100);
		assertThat(tested.getEnd()).isEqualTo(100);
	}
	
	@Test
	void startNegative(){
		var tested = getProgressBar();
		assertThatThrownBy(() -> tested.setStart(-10)).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void startTooBig(){
		var tested = getProgressBar();
		assertThatThrownBy(() -> tested.setStart(10)).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void endNegative(){
		var tested = getProgressBar();
		assertThatThrownBy(() -> tested.setEnd(-10)).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void endTooSmall(){
		var tested = getProgressBar();
		tested.setEnd(100);
		tested.setStart(20);
		
		assertThatThrownBy(() -> tested.setEnd(10)).isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void currentNegative(){
		var tested = getProgressBar();
		tested.setEnd(100);
		tested.setStart(20);
		
		assertThatThrownBy(() -> tested.setCurrent(-10)).isInstanceOf(IllegalArgumentException.class);
	}
}