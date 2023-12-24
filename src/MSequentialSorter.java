

import java.util.ArrayList;
import java.util.List;

/**
 * MSequentialSorter uses the merge sort algorithm, which recursively splits the list into smaller sublists, 
 * sorts the sublists, then merges them back into a single list.
 */
public class MSequentialSorter implements Sorter {

  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
	  if (list.isEmpty()) {return List.of();}
	  return sortRecursive(list);
  }
  
  private <T extends Comparable<? super T>> List<T> sortRecursive(List<T> list) {
	  // Delegate to Insertion sort for small arrays. This is a common optimisation, and it saves us a
	  // noticeable amount of time.
	  if (list.size() < 20) return SortUtil.insSorter.sort(list);
	  int endpoint = list.size();
	  // Get the index which is roughly halfway through the list.
	  // For odd numbered lists, the first half will always be the shorter one.
	  int halfway =  endpoint / 2;
	  
	  // Recursively split both halves.
	  List<T> firstHalf = sortRecursive(list.subList(0, halfway));
	  List<T> secondHalf = sortRecursive(list.subList(halfway, endpoint));
	  
	  // Merge and sort the two halves together.
	  return merge(firstHalf, secondHalf);
  }
  
  public <T extends Comparable<? super T>> List<T> merge(List<T> firstHalf, List<T> lastHalf) {
	  // Check if the halves already in sorted order, if so, don't merge, and instead just join the lists.
	  // (This is another well known optimisation and it appears to save us some time according to my testing).
	  if (firstHalf.get(firstHalf.size()-1).compareTo(lastHalf.get(0)) <= 0) {
		  firstHalf.addAll(lastHalf);
		  return firstHalf;
	  }
	  int i = 0; // The index for first half.
	  int j = 0; // The index for last half.
	  List<T> bothHalves = new ArrayList<T>();
	  // For each element in the firstHalf, compare it with the corresponding element in the secondHalf, 
	  // and add the lesser element to the bothHalves List.
	  while (i < firstHalf.size() && j < lastHalf.size()) {
		  T e1 = firstHalf.get(i);
		  T e2 = lastHalf.get(j);
		  if (e1.compareTo(e2) < 0) {
			  bothHalves.add(e1);
			  i++;
		  } else {
			  bothHalves.add(e2);
			  j++;
		  }
	  }
	  // Once we reach here, there may be one half which still contains elements. These elements are bigger than
	  // all those in the bothHalves List, and are in sorted order, so we can just append them to bothHalves.
	  while (i < firstHalf.size()) {
		  bothHalves.add(firstHalf.get(i));
		  i++;
	  }
	  while (j < lastHalf.size()) {
		  bothHalves.add(lastHalf.get(j));
		  j++;
	  }
	  return bothHalves;
  }
  
  

}