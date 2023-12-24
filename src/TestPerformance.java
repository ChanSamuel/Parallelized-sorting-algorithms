

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Pros:
 * 1). Warmup helps test performance to not be skewed by JIT not working.
 * 2). Extensible. Can easily add a test for a new type of dataset by adding a new testX() method, or 
 * add a new type of sorter by adding an extra msg(Sorter s, ...) in the msgAll() method.
 * 
 * Cons:
 * 1). Assumes fixed input size, msgAll() does not differentiate the test performance for smaller/bigger datasets. 
 * This is bad because we want to see the performance of these algorithms on a range of typical workloads.
 * 2). The msg() method prints the time taken to do 200 runs, instead of outputting the individual
 * times taken for each of the 200 runs (i.e., we are compressing the test statistic into a single figure).
 * This could be bad if one of our algorithms are broken in such a way that it produces outlying times
 * which skew our the overall time taken to do 200 runs. In such situation, our test is hiding the fact that
 * our algorithm is broken.
 * 3). Doesn't measure memory footprint. Our algorithm may perform well time wise but use alot of memory, or
 * may not perform well due to a high rate of garbage being generated. In any case, it is useful to know the
 * space cost of our algorithms when considering performance. 
 * 
 * Alternative implementation suggestions:
 * 1). Use a benchmark tool like jmh.
 * 2). Create another TestPerformance implementation where the msg() method writes a file with the 
 * individual times taken to do each of the 200 runs. 
 * 3). Create another seperate implemenation to also report the time taken to sort each T[] in the dataset, as 
 * this allows us to compare the algorithms on items of the same dataset (e.g., perhaps one algorithm may
 * take longer to sort reverse ordered lists than another).
 * 
 * In summary, it may be better to have multiple TestPerformance implementations which report different
 * statistics about our algorithms.
 *
 */
public class TestPerformance {
	
	/**
	 * Measures the time taken to complete all runs of the Runnable after a warmup period.
	 * Given a Runnable which sorts all items in the dataset, we define each run to be a single call of
	 * r.run(). 
	 * 
	 * In order to get the JIT compiler working ('Just In Time' compiler improves performance at runtime), 
	 * 'warmUp' many runs are done before doing the actual timed runs.
	 * The returned value, is the time taken to do the specified many runs (i.e., does not include warm up).
	 * 
	 * System.gc() is called at the beginning of this method to attempt garbage collection.
	 * 
	 * @param r : A Runnable whose run() method sorts each item in the dataset.
	 * @param warmUp : The number of times to run the warm up should be in the tens of thousands.
	 * @param runs : The number of times to run the performance test.
	 * @return how much time in milliseconds all runs took.
     */
  long timeOf(Runnable r,int warmUp,int runs) {
    System.gc();
    for(int i=0;i<warmUp;i++) {r.run();}
    long time0=System.currentTimeMillis();
    for(int i=0;i<runs;i++) {r.run();}
    long time1=System.currentTimeMillis();
    return time1-time0;
  }
  
  /**
   * Given a Sorter and dataset, prints the time taken to do 200 runs after a 20,000 run warm up.
   * The time taken to do a single run is the time taken to sort each item in the dataset with the given
   * Sorter.
   * 
   * @param <T> : The Comparable datatype that we are sorting with.
   * @param s : The Sorter used to sort the dataset.
   * @param name : The name of the sorting algorithm used.
   * @param dataset : An array where each element is an T[] which needs to be sorted from low to high.
   */
  <T extends Comparable<? super T>>void msg(Sorter s,String name,T[][] dataset) {
    long time=timeOf(()->{
      for(T[]l:dataset){s.sort(Arrays.asList(l));}
      },20000,200);//realistically 20.000 to make the JIT do his job..
      System.out.println(name+" sort takes "+time/1000d+" seconds");
    }
  
  /**
   * Tests the performance for all Sorter types for a particular dataset.
   * Does this by calling msg() for each Sorter type, which prints out the performance of the Sorter
   * for this dataset (for a given number of runs).
   * The dataset should be an array of T[] where each T[] is an array to be sorted.
   * 
   * @param <T> : The Comparable datatype that we are sorting with.
   * @param dataset : An array where each element is an T[] which needs to be sorted from low to high.
   */
  <T extends Comparable<? super T>>void msgAll(T[][] dataset) {
    //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
    //uncomment the former line to include performance of ISequentialSorter
    msg(new MSequentialSorter(),"Sequential merge sort",dataset);
    msg(new MParallelSorter1(),"Parallel merge sort (futures)",dataset);
    msg(new MParallelSorter2(),"Parallel merge sort (completablefutures)",dataset);
    msg(new MParallelSorter3(),"Parallel merge sort (forkJoin)",dataset);
    }
  
  /**
   * Test performance of all sorting algorithms using the BigInteger dataset.
   */
  @Test
  void testBigInteger() {
    System.out.println("On the data type BigInteger");
    msgAll(TestBigInteger.dataset);
    }
  
  /**
   * Test performance of all sorting algorithms using the Float dataset.
   */
  @Test
  void testFloat() {
    System.out.println("On the data type Float");
    msgAll(TestFloat.dataset);
    }
  
  /**
   * Test performance of all sorting algorithms using the Point dataset.
   */
  @Test
  void testPoint() {
    System.out.println("On the data type Point");
    msgAll(TestPoint.dataset);
    }
  
  /**
   * Test performance of all sorting algorithms using the Double dataset.
   */
  @Test
  void testDouble() {
	  System.out.println("On the data type Double");
	  msgAll(TestDouble.dataset);
  }
  
  }