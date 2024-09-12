package fr.rakambda.progressbar;

import fr.rakambda.progressbar.api.bar.IProgressBar;
import fr.rakambda.progressbar.impl.bar.ComposedProgressBar;
import fr.rakambda.progressbar.impl.bar.CumulativeProgressBar;
import fr.rakambda.progressbar.impl.bar.SimpleProgressBar;
import fr.rakambda.progressbar.impl.holder.ProgressBarHolder;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("NewClassNamingConvention")
class ShowcaseDemo {
	@Test
	void run() throws Exception{
		try(var holder = ProgressBarHolder.builder().build()){
			var composedBar = holder.addProgressBar(CumulativeProgressBar.builder()
					.name("group-task-1")
					.removeWhenComplete(true)
					.build());
			var composedBar2 = composedBar.addProgressBar(ComposedProgressBar.builder()
					.name("group-task-2")
					.removeWhenComplete(true)
					.build());
			
			var bars = new LinkedList<IProgressBar>();
			for(int i = 0; i < 5; i++){
				bars.add(composedBar.addProgressBar(SimpleProgressBar.builder()
						.name("test-" + i)
						.end(new AtomicLong(100))
						.hideWhenComplete(true)
						.description("desc")
						.unit("ut")
						.build()));
			}
			for(int i = 0; i < 5; i++){
				bars.add(composedBar2.addProgressBar(SimpleProgressBar.builder()
						.name("test-" + (i + 5))
						.end(new AtomicLong(100))
						.hideWhenComplete(true)
						.showPercentage(true)
						.build()));
			}
			for(int i = 0; i < 10; i++){
				bars.add(holder.addProgressBar(SimpleProgressBar.builder()
						.name("test-" + (i + 10))
						.end(new AtomicLong(100))
						.removeWhenComplete(true)
						.build()));
			}
			
			Thread.sleep(2000);
			
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < bars.size(); j++){
					bars.get(j).increment(((j + 4) / 4) * 5);
					if(j % 2 == 0){
						bars.get(j).setDescription("Step " + i);
					}
				}
				Thread.sleep(1000);
			}
			
			Thread.sleep(4000);
		}
	}
}
