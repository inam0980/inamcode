class Solution {
    public int minElement(int[] nums) {
        int minVal = Integer.MAX_VALUE; // Initialize minVal to the largest possible integer value

        // Iterate through each number in the input array
        for (int num : nums) {
            int sumOfDigits = 0; // Initialize sum for current number

            // Calculate the sum of digits for the current number
            // This loop continues as long as num is greater than 0
            while (num > 0) {
                sumOfDigits += num % 10; // Add the last digit to sumOfDigits
                num /= 10;               // Remove the last digit from num
            }
            
            // Update minVal if the current sumOfDigits is smaller
            if (sumOfDigits < minVal) {
                minVal = sumOfDigits;
            }
        }

        return minVal; // Return the overall minimum sum of digits found
    }
}
// Time Complexity: O(N * log(Max_Num)), where N is the length of nums and Max_Num is the maximum value in nums.
// The log(Max_Num) factor comes from calculating the sum of digits for each number.
// Space Complexity: O(1)