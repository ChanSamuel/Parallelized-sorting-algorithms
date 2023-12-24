

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * MParallelSorter3 implements a non-blocking version of Merge Sort using the ForkJoin framework.
 *
 */
public class MParallelSorter3 implements Sorter {
	
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		if (list.isEmpty()) {List.of();}
		return new SorterTask<T>(list).invoke();
	}

}

class SorterTask<U extends Comparable<? super U>> extends RecursiveTask<List<U>> {
	private List<U> elements;

	public SorterTask(List<U> elements) {
		this.elements = elements;
	}

	@Override
	protected List<U> compute() {
		if (elements.size() < 20) {return SortUtil.seqSorter.sort(elements);}
		int endpoint = elements.size();
		int halfway =  endpoint / 2;
		// Here we create two tasks for both halves, invokeAll() will then delegate one task, and do the other.
		// Once both tasks are done, then we merge.
		SorterTask<U> left = new SorterTask<U>(elements.subList(0, halfway));
		SorterTask<U> right = new SorterTask<U>(elements.subList(halfway, endpoint));
		invokeAll(left, right);
		return SortUtil.seqSorter.merge(left.join(), right.join()); // At this point both tasks should be done.
	}

}