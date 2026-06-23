class Solution {
    public int minMirrorPairDistance(int[] nums) {
        // Map to store each number and a list of indices where it appears
        // This helps in finding the minimum distance efficiently when a reverse is found
        java.util.Map<Integer, java.util.List<Integer>> numToIndices = new java.util.HashMap<>();

        // Populate the map with numbers and their indices
        for (int i = 0; i < nums.length; i++) {
            numToIndices.computeIfAbsent(nums[i], k -> new java.util.ArrayList<>()).add(i);
        }

        int minDistance = Integer.MAX_VALUE;

        // Iterate through each number in the input array
        for (int i = 0; i < nums.length; i++) {
            int currentNum = nums[i];
            int reversedNum = reverse(currentNum);

            // If the reversed number exists in the map and it's not the current number itself
            // (unless currentNum is a palindrome and we need to check for other occurrences)
            if (numToIndices.containsKey(reversedNum)) {
                // Get all indices where the reversed number appears
                java.util.List<Integer> indicesOfReversed = numToIndices.get(reversedNum);

                // Iterate through these indices to find potential mirror pairs
                for (int j : indicesOfReversed) {
                    // A valid mirror pair (i, j) must have i < j
                    if (i < j) {
                        minDistance = Math.min(minDistance, j - i);
                    }
                }
            }
        }

        // If minDistance is still MAX_VALUE, no mirror pair was found
        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    // Helper function to reverse an integer
    private int reverse(int x) {
        int reversed = 0;
        while (x > 0) {
            reversed = reversed * 10 + (x % 10);
            x /= 10;
        }
        return reversed;
    }
}

// Time complexity: O(N * log(max_num_digits) + N * K) where N is nums.length, log(max_num_digits) is for reversing each number, and K is the maximum number of occurrences of any reversed number. In the worst case, K can be N, leading to O(N^2). However, for typical distributions, K is small. A more precise analysis for the map iteration is O(N) on average if we consider the total number of entries in all lists. The dominant factor is usually the initial population and the reverse operation.
// Space complexity: O(N) for storing numbers and their indices in the hash map.