import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sreenaveen
 *
 */
public class Searching {

	public boolean collectionSetSearch(int arr[], Integer numberToSearch) {
		Set<Integer> intSet = new HashSet<Integer>();
		for (int i : arr) {
			intSet.add(i);
		}
		return intSet.contains(numberToSearch);
	}

	public boolean arraysSearch(int arr[], int numberToSearch) {

		int retVal = Arrays.binarySearch(arr, numberToSearch);
		if (retVal != -1)
			return true;
		else
			return false;
	}

	public  boolean normalSearch(int[] arr, int item) {
	      for (int n : arr) {
	         if (item == n) {
	            return true;
	         }
	      }
	      return false;
	   }

	public static void main(String args[]) throws IOException {
		Searching obj = new Searching();
		int[] list_of_numbers = { 9, 55, 10, 12, 23, 244, 2131 };

		obj.collectionSetSearch(list_of_numbers, 23);

		obj.arraysSearch(list_of_numbers, 45);
		obj.normalSearch(list_of_numbers, 45);

	}
}
