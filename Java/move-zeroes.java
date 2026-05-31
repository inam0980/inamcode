class Solution {
    public void moveZeroes(int[] nums) {
        int nonZeroPointer = 0; // Pointer for placing non-zero elements

        // Iterate through the array with a current pointer
        for (int current = 0; current < nums.length; current++) {
            // If the current element is non-zero, place it at nonZeroPointer's position
            // and increment nonZeroPointer
            if (nums[current] != 0) {
                nums[nonZeroPointer] = nums[current];
                nonZeroPointer++;
            }
        }

        // After placing all non-zero elements, fill the rest of the array with zeros
        while (nonZeroPointer < nums.length) {
            nums[nonZeroPointer] = 0;
            nonZeroPointer++;
        }
    }
}
// Time complexity: O(n) where n is the number of elements in nums. We iterate through the array twice in the worst case (once to move non-zeros, once to fill zeros).
// Space complexity: O(1) as we are modifying the array in-place without using any extra data structures.