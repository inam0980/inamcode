class Solution {
    public int subarraySum(int[] nums) {
        int n = nums.length;
        long totalSum = 0; // Use long to prevent potential overflow for total sum

        // Calculate prefix sums to efficiently query subarray sums
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        // Iterate through each index i to define its specific subarray
        for (int i = 0; i < n; i++) {
            // Determine the start index for the subarray nums[start ... i]
            // start = max(0, i - nums[i])
            int start = Math.max(0, i - nums[i]);
            
            // Calculate the sum of the subarray nums[start ... i] using prefix sums
            // Sum(nums[start ... i]) = prefixSum[i + 1] - prefixSum[start]
            totalSum += (prefixSum[i + 1] - prefixSum[start]);
        }

        return (int) totalSum; // Cast back to int as per problem return type
    }
}
// Time Complexity: O(n)
// Space Complexity: O(n)