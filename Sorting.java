import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sreenaveen
 *
 */
public class Sorting {

	/**
	 * @param arr
	 * @return
	 */
	int[] bubbleSort(int arr[]) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (arr[j] > arr[j + 1]) {
					// swap temp and arr[i]
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
		return arr;
	}

	/**
	 * @param arr
	 * @return
	 */
	public List<Integer> mergeSort(int arr[]) {

		List<Integer> intList = new ArrayList<Integer>();
		for (int i : arr) {
			intList.add(i);
		}
		Collections.sort(intList);

		return intList;
	}

	/**
	 * @param arr
	 * @return
	 */
	public int[] quickSort(int arr[]) {

		Arrays.sort(arr);

		return arr;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		Sorting obj = new Sorting();
		int[] list_of_numbers = { 9, 55, 10, 12, 23, 244, 2131 };

		obj.bubbleSort(list_of_numbers);

		obj.mergeSort(list_of_numbers);

		obj.quickSort(list_of_numbers);

	}
}
