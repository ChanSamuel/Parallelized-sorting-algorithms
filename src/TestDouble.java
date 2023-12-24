
import java.util.Random;

import org.junit.jupiter.api.Test;

public class TestDouble {
	public static final Double[][] dataset = {
			{ 1d, 2d, 3d, 4d, 5d, 6d }, 
			{ 8d, 7d, 6d, 5d, 4d, 3d },
			{ -1d, -2d, -3d, -4d, -5d, -6d },
			{ 0d, Double.MAX_VALUE, Double.MIN_VALUE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY },
			{ Double.NaN, 0d, 0d, -1d, 1d, 2d }, 
			{}, 
			manyOrdered(10000), manyReverse(10000), manyRandom(10000)};

	static private Double[] manyRandom(int size) {
		Random r = new Random(0);
		Double[] result = new Double[size];
		for (int i = 0; i < size; i++) {
			result[i] = r.nextDouble();
		}
		return result;
	}

	static private Double[] manyReverse(int size) {
		Double[] result = new Double[size];
		for (int i = 0; i < size; i++) {
			result[i] = (size - i) + 0.42d;
		}
		return result;
	}

	static private Double[] manyOrdered(int size) {
		Double[] result = new Double[size];
		for (int i = 0; i < size; i++) {
			result[i] = i + 0.42d;
		}
		return result;
	}

	@Test
	public void testISequentialSorter() {
		Sorter s = new ISequentialSorter();
		for (Double[] l : dataset) {
			TestHelper.testData(l, s);
		}
	}

	@Test
	public void testMSequentialSorter() {
		Sorter s = new MSequentialSorter();
		for (Double[] l : dataset) {
			TestHelper.testData(l, s);
		}
	}

	@Test
	public void testMParallelSorter1() {
		Sorter s = new MParallelSorter1();
		for (Double[] l : dataset) {
			TestHelper.testData(l, s);
		}
	}

	@Test
	public void testMParallelSorter2() {
		Sorter s = new MParallelSorter2();
		for (Double[] l : dataset) {
			TestHelper.testData(l, s);
		}
	}

	@Test
	public void testMParallelSorter3() {
		Sorter s = new MParallelSorter3();
		for (Double[] l : dataset) {
			TestHelper.testData(l, s);
		}
	}
}
