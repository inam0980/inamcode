class Solution {
    public int minElement(int[] nums) {
        // Initialize minOverallSum with the largest possible integer value
        // to ensure any valid digit sum will be smaller.
        int minOverallSum = Integer.MAX_VALUE;

        // Iterate through each number in the input array.
        for (int num : nums) {
            int currentNum = num; // Use a temporary variable to avoid modifying the original num.
            int digitSum = 0;     // Initialize sum of digits for the current number.

            // Calculate the sum of digits for the current number.
            // This loop continues as long as there are digits left in currentNum.
            while (currentNum > 0) {
                digitSum += current