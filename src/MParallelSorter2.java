

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * MParallelSorter2 implements a non-blocking version of parallel merge sort
 * using CompletableFutures.
 *
 */
public class MParallelSorter2 implements Sorter {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		if (list.isEmpty()) {
			return List.of();
		}
		return sortRec(list).join(); // Block the main thread until the final merge is done.
	}

	public <T extends Comparable<? super T>> CompletableFuture<List<T>> sortRec(List<T> list) {
		if (list.size() < 20) {
			return CompletableFuture.supplyAsync(() -> SortUtil.seqSorter.sort(list));
		}
		int endpoint = list.size();
		int halfway = endpoint / 2;
		// Delegate both halves, and only delegate the merge when both halves are done.
		CompletableFuture<List<T>> left = sortRec(list.subList(0, halfway));
		CompletableFuture<List<T>> right = sortRec(list.subList(halfway, endpoint));
		CompletableFuture<List<T>> merged = left.thenCombine(right, (l, r) -> SortUtil.seqSorter.merge(l, r));
		return merged; // We return a CompletableFuture of the result rather than waiting.
	}

}