import java.util.Arrays;

class Solution {
    public double trimMean(int[] arr) {
        // Sort the array to easily identify smallest and largest elements
        Arrays.sort(arr);

        int n = arr.length;
        // Calculate the number of elements to remove from each end (5% of total)
        int elementsToRemove = n / 20; // 5% of n is n * 5 / 100 = n / 20

        double sum = 0;
        // Iterate from the (5% removed) index to the (100% - 5% removed) index
        // This effectively skips the smallest 5% and largest 5%
        for (int i = elementsToRemove; i < n - elementsToRemove; i++) {
            sum += arr[i];
        }

        // The number of remaining elements is n - (2 * elementsToRemove)
        int remainingElementsCount = n - (2 * elementsToRemove);

        // Calculate and return the mean
        return sum / remainingElementsCount;
    }
}
// Time Complexity: O(N log N) due to sorting the array.
// Space Complexity: O(log N) or O(N) depending on the sorting algorithm implementation (e.g., Timsort in Java uses O(log N) for object arrays and O(N) in worst case for primitives).