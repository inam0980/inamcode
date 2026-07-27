class Solution {
    public int maxProduct(int[] nums) {
        int max1 = 0; // Stores the largest number
        int max2 = 0; // Stores the second largest number

        // Iterate through the array to find the two largest numbers
        for (int num : nums) {
            if (num > max1) {
                max2 = max1; // Current max1 becomes the second largest
                max1 = num;  // Current num becomes the new largest
            } else if (num > max2) {
                max2 = num;  // Current num becomes the new second largest
            }
        }

        // Calculate and return the maximum product
        return (max1 - 1) * (max2 - 1);
    }
}
// Time complexity: O(n)
// Space complexity: O(1)