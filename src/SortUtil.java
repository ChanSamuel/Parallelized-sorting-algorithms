

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SortUtil {
	public static final ExecutorService pool = Executors.newWorkStealingPool();
	public static final MSequentialSorter seqSorter = new MSequentialSorter();
	public static final ISequentialSorter insSorter = new ISequentialSorter();
}
