class Solution {
    public int minElement(int[] nums) {
        int minDigitSum = Integer.MAX_VALUE; // Initialize minimum digit sum to a very large value

        for (int num : nums) {
            int currentDigitSum = 0;
            int temp = num; // Use a temporary variable to avoid modifying the original number during digit sum calculation

            // Calculate the sum of digits for the current number
            while (temp > 0) {
                currentDigitSum += temp % 10; // Add the last digit
                temp /= 10; // Remove the last digit
            }

            // Update the minimum digit sum if the current digit sum is smaller
            minDigitSum = Math.min(minDigitSum, currentDigitSum);
        }

        return minDigitSum; // Return the overall minimum digit sum found
    }
}
// Time Complexity: O(N * log10(M)), where N is the length of nums and M is the maximum value in nums.
// The log10(M) factor comes from calculating the sum of digits for each number.
// Space Complexity: O(1), as we only use a few extra variables.