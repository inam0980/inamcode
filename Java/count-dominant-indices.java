class Solution {
    public int dominantIndices(int[] nums) {
        int n = nums.length;
        int dominantCount = 0;

        // Iterate from i = 0 up to n - 2 (as the rightmost element cannot be dominant)
        for (int i = 0; i < n - 1; i++) {
            long sum = 0; // Use long to prevent overflow for sum, though not strictly necessary given constraints
            int count = 0;

            // Calculate sum and count for elements to the right of nums[i]
            for (int j = i + 1; j < n; j++) {
                sum += nums[j];
                count++;
            }

            // If there are elements to the right, calculate average and check dominance
            if (count > 0) {
                // To avoid floating-point precision issues, compare nums[i] * count with sum
                // nums[i] > sum / count  is equivalent to nums[i] * count > sum
                if ((long)nums[i] * count > sum) {
                    dominantCount++;
                }
            }
        }

        return dominantCount;
    }
}
// Time Complexity: O(n^2) - The outer loop runs n-1 times, and the inner loop runs up to n-1 times.
// Space Complexity: O(1) - Only a few constant extra variables are used.