package fr.rakambda.progressbar.impl.holder;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.api.update.IProgressBarTask;
import fr.rakambda.progressbar.impl.update.DefaultProgressBarTask;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProgressBarHolderTest{
	@Mock
	private ScheduledExecutorService scheduledExecutorService;
	@Mock
	private IProgressBarTask<IProgressBar> progressBarTask;
	@Mock
	private ScheduledFuture<?> future;
	
	@BeforeEach
	void setUp(){
		doReturn(future).when(scheduledExecutorService).scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
	}
	
	private ProgressBarHolder buildProgressBarHolder(){
		return ProgressBarHolder.builder()
				.executorService(scheduledExecutorService)
				.progressBarTask(progressBarTask)
				.build();
	}
	
	@Test
	void taskIsScheduled(){
		buildProgressBarHolder();
		verify(scheduledExecutorService).scheduleWithFixedDelay(eq(progressBarTask), eq(0L), anyLong(), eq(TimeUnit.MILLISECONDS));
	}
	
	@Test
	void taskIsScheduledWithCustomDelay(){
		var delay = 3000L;
		
		ProgressBarHolder.builder()
				.executorService(scheduledExecutorService)
				.refreshRate(delay)
				.build();
		
		verify(scheduledExecutorService).scheduleWithFixedDelay(any(DefaultProgressBarTask.class), eq(0L), eq(delay), eq(TimeUnit.MILLISECONDS));
	}
	
	@Test
	void addedProgressBarIsPropagatedToTask(){
		var holder = buildProgressBarHolder();
		
		var bar = mock(IProgressBar.class);
		holder.addProgressBar(bar);
		
		verify(progressBarTask).addProgressBar(bar);
	}
	
	@Test
	void removedProgressBarIsPropagatedToTask(){
		var holder = buildProgressBarHolder();
		
		var bar = mock(IProgressBar.class);
		holder.removeProgressBar(bar);
		
		verify(progressBarTask).removeProgressBar(bar);
	}
	
	@Test
	void closeCleansUp() throws Exception{
		try(var ignored = buildProgressBarHolder()){
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		
		verify(scheduledExecutorService).shutdown();
		verify(future).cancel(true);
		verify(progressBarTask).close();
	}
}