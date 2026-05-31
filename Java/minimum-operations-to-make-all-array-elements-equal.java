import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class Solution {
    public List<Long> minOperations(int[] nums, int[] queries) {
        Arrays.sort(nums); // Sort nums to efficiently calculate prefix sums and find split point
        int n = nums.length;
        long[] prefixSum = new long[n + 1]; // Prefix sums for sorted nums
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        List<Long> results = new ArrayList<>();
        for (int query : queries) {
            // Find the index 'idx' where nums[idx-1] < query and nums[idx] >= query
            // All elements before idx need to be increased to query
            // All elements from idx onwards need to be decreased to query
            int idx = Arrays.binarySearch(nums, query);
            if (idx < 0) {
                idx = -(idx + 1); // Convert negative insertion point to actual index
            }

            long operations = 0;
            // Operations for elements less than query
            // There are 'idx' elements less than query (nums[0]...nums[idx-1])
            // Sum of these elements is prefixSum[idx]
            // Each needs to be increased to 'query', total increase: idx * query
            // Operations = (idx * query) - sum(nums[0]...nums[idx-1])
            operations += (long) idx * query - prefixSum[idx];

            // Operations for elements greater than or equal to query
            // There are 'n - idx' elements from nums[idx]...nums[n-1]
            // Sum of these elements is prefixSum[n] - prefixSum[idx]
            // Each needs to be decreased to 'query', total decrease: (n - idx) * query
            // Operations = sum(nums[idx]...nums[n-1]) - ((n - idx) * query)
            operations += (prefixSum[n] - prefixSum[idx]) - (long) (n - idx) * query;
            
            results.add(operations);
        }

        return results;
    }
}
// Time complexity: O(N log N + M log N) where N is nums.length and M is queries.length.
// Sorting nums takes O(N log N).
// Calculating prefix sums takes O(N).
// For each query, binary search takes O(log N) and constant time calculations. Total O(M log N).
// Space complexity: O(N) for prefixSum array.