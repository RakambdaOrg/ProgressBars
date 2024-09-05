package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.impl.ParallelizableTest;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ParallelizableTest
class ComposedProgressBarTest extends BaseProgressBarTest<ComposedProgressBar>{
	private static final int END_VALUE = 10;
	
	@Override
	protected ComposedProgressBar getProgressBar(){
		return ComposedProgressBar.builder().build();
	}
	
	@Test
	void defaultValues(){
		var tested = getProgressBar();
		assertThat(tested.getStart()).isEqualTo(0);
		assertThat(tested.getCurrent()).isEqualTo(0);
		assertThat(tested.getEnd()).isEqualTo(0);
	}
	
	@Test
	void addNonCompletedBars(){
		var tested = getProgressBar();
		
		tested.addProgressBar(generateBar(0));
		tested.addProgressBar(generateBar(0));
		
		assertThat(tested.getChildren()).hasSize(2);
		assertThat(tested.getStart()).isEqualTo(0);
		assertThat(tested.getCurrent()).isEqualTo(0);
		assertThat(tested.getEnd()).isEqualTo(2);
	}
	
	@Test
	void addSomeCompletedBars(){
		var tested = getProgressBar();
		
		tested.addProgressBar(generateBar(END_VALUE));
		tested.addProgressBar(generateBar(0));
		
		assertThat(tested.getChildren()).hasSize(2);
		assertThat(tested.getStart()).isEqualTo(0);
		assertThat(tested.getCurrent()).isEqualTo(1);
		assertThat(tested.getEnd()).isEqualTo(2);
	}
	
	@Test
	void addAllCompletedBars(){
		var tested = getProgressBar();
		
		tested.addProgressBar(generateBar(END_VALUE));
		tested.addProgressBar(generateBar(END_VALUE));
		
		assertThat(tested.getChildren()).hasSize(2);
		assertThat(tested.getStart()).isEqualTo(0);
		assertThat(tested.getCurrent()).isEqualTo(2);
		assertThat(tested.getEnd()).isEqualTo(2);
	}
	
	@Test
	void cannotSetStart(){
		assertThatThrownBy(() -> getProgressBar().setStart(1)).isInstanceOf(UnsupportedOperationException.class);
	}
	
	@Test
	void cannotSetCurrent(){
		assertThatThrownBy(() -> getProgressBar().setCurrent(1)).isInstanceOf(UnsupportedOperationException.class);
	}
	
	@Test
	void cannotSetEnd(){
		assertThatThrownBy(() -> getProgressBar().setEnd(1)).isInstanceOf(UnsupportedOperationException.class);
	}
	
	private IProgressBar generateBar(long progress){
		return SimpleProgressBar.builder()
				.current(new AtomicLong(progress))
				.end(new AtomicLong(END_VALUE))
				.build();
	}
}