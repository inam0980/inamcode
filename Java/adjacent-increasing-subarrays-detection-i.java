import java.util.List;

class Solution {
    public boolean hasIncreasingSubarrays(List<Integer> nums, int k) {
        int n = nums.size();

        // Iterate through all possible starting positions 'a' for the first subarray
        // The first subarray is nums[a...a+k-1]
        // The second subarray is nums[a+k...a+2k-1]
        // So, a + 2k - 1 must be less than n
        for (int a = 0; a <= n - 2 * k; a++) {
            boolean firstSubarrayIncreasing = true;
            // Check if the first subarray nums[a...a+k-1] is strictly increasing
            for (int i = 0; i < k - 1; i++) {
                if (nums.get(a + i) >= nums.get(a + i + 1)) {
                    firstSubarrayIncreasing = false;
                    break;
                }
            }

            if (firstSubarrayIncreasing) {
                // If the first subarray is increasing, check the second adjacent subarray
                int b = a + k; // Starting index of the second subarray
                boolean secondSubarrayIncreasing = true;
                // Check if the second subarray nums[b...b+k-1] is strictly increasing
                for (int i = 0; i < k - 1; i++) {
                    if (nums.get(b + i) >= nums.get(b + i + 1)) {
                        secondSubarrayIncreasing = false;
                        break;
                    }
                }

                if (secondSubarrayIncreasing) {
                    return true; // Found two adjacent strictly increasing subarrays
                }
            }
        }

        return false; // No such pair of subarrays found
    }
}
// Time complexity: O(N*K) where N is the length of nums. The outer loop runs N-2K+1 times, and inner loops run K times.
// Space complexity: O(1)