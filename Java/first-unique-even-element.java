class Solution {
    public int firstUniqueEven(int[] nums) {
        // Map to store frequency of each number
        int[] counts = new int[101]; // Constraints: 1 <= nums[i] <= 100

        // First pass: populate frequencies
        for (int num : nums) {
            counts[num]++;
        }

        // Second pass: find the first unique even number
        for (int num : nums) {
            // Check if the number is even and appears exactly once
            if (num % 2 == 0 && counts[num] == 1) {
                return num; // Return the first such number encountered
            }
        }

        // If no unique even number is found
        return -1;
    }
}

// Time complexity: O(N) where N is the length of the nums array.
// We iterate through the array twice. The frequency array operations are O(1) due to fixed size.
// Space complexity: O(C) where C is the range of possible values in nums (101 in this case).
// This is effectively O(1) because the size of the counts array is constant and not dependent on N.