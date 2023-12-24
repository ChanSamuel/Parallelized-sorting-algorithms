

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * MParallelSorter1 implements a blocking version of parallel merge sort using Futures.
 * Note: A work stealing thread pool is used in this Sorter, meaning that it's technically non-blocking. 
 *
 */
public class MParallelSorter1 implements Sorter {
	
	private <T> T get(Future<T> f) {
		try {
			return f.get(); // Get the value from the future if the task is done, if not, then block until it is.
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new Error(e);
		} catch (ExecutionException e) { 
			Throwable t = e.getCause();
			if (t instanceof RuntimeException) {throw (RuntimeException) t;}
			if (t instanceof Error) {throw (Error) t;}
			throw new Error("Unexpected exception occured", t);
		}
	}

  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
	  if (list.isEmpty()) {List.of();}
	  return sortRecursive(list);
  }
  
  private <T extends Comparable<? super T>> List<T> sortRecursive(List<T> list) {
	  if (list.size() < 20) {return SortUtil.seqSorter.sort(list);}
	  int endpoint = list.size();
	  // Get the index which is roughly halfway through the list.
	  // For odd numbered lists, the first half will always be the shorter one.
	  int halfway =  endpoint / 2;
	  
	  // Split both halves, by delegating one half, and doing the other ourselves.
	  Future<List<T>> firstHalfFuture = SortUtil.pool.submit(() -> sortRecursive(list.subList(0, halfway)));
	  List<T> secondHalf = sortRecursive(list.subList(halfway, endpoint));
	  
	  // Merge and sort both halves, and block if firstHalf not done yet.
	  return SortUtil.seqSorter.merge(get(firstHalfFuture), secondHalf);
  }

}