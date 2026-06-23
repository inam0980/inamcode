class Solution {
    public int maxGoodNumber(int[] nums) {
        int maxVal = 0;

        // All possible permutations for 3 elements
        int[][] permutations = {
            {0, 1, 2}, {0, 2, 1},
            {1, 0, 2}, {1, 2, 0},
            {2, 0, 1}, {2, 1, 0}
        };

        for (int[] p : permutations) {
            StringBuilder sb = new StringBuilder();
            // Concatenate binary representations in current permutation order
            sb.append(Integer.toBinaryString(nums[p[0]]));
            sb.append(Integer.toBinaryString(nums[p[1]]));
            sb.append(Integer.toBinaryString(nums[p[2]]));
            
            // Convert the concatenated binary string to an integer
            // Use base 2 for parsing binary string
            maxVal = Math.max(maxVal, Integer.parseInt(sb.toString(), 2));
        }

        return maxVal;
    }
}
// Time Complexity: O(1) because the input array size is fixed at 3. Specifically, it's O(3! * L) where L is the max length of binary string.
// Space Complexity: O(1) because the storage used is constant regardless of input values (fixed array size, fixed number of permutations).