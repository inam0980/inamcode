class Solution {
    public int minimizeTheDifference(int[][] mat, int target) {
        // Max possible sum: m * max_val = 70 * 70 = 4900
        // Max target: 800. We need to consider sums up to max_sum.
        // Using a boolean array to store reachable sums.
        // dp[i] is true if sum i is reachable.
        boolean[] dp = new boolean[4901]; 
        dp[0] = true; // Base case: sum 0 is reachable before processing any rows

        int maxReachableSum = 0; // Tracks the maximum sum currently reachable

        for (int[] row : mat) {
            boolean[] nextDp = new boolean[4901];
            int currentMaxRowSum = 0; // Max value in current row
            for (int val : row) {
                currentMaxRowSum = Math.max(currentMaxRowSum, val);
            }

            // Iterate through all previously reachable sums
            for (int prevSum = 0; prevSum <= maxReachableSum; prevSum++) {
                if (dp[prevSum]) {
                    // For each previously reachable sum, try adding each element from the current row
                    for (int val : row) {
                        int newSum = prevSum + val;
                        // Only consider sums up to target + max_val_in_row + some buffer
                        // or max_possible_sum to avoid out of bounds and unnecessary computations.
                        // A tighter bound could be target + max_val_in_row, but 4900 is safe.
                        if (newSum <= 4900) { 
                            nextDp[newSum] = true;
                        }
                    }
                }
            }
            dp = nextDp; // Update dp array for the next row
            maxReachableSum += currentMaxRowSum; // Update max reachable sum
            // Cap maxReachableSum to 4900 to avoid index out of bounds if not already capped
            maxReachableSum = Math.min(maxReachableSum, 4900);
        }

        int minDiff = Integer.MAX_VALUE;
        // After processing all rows, find the sum closest to target
        for (int sum = 0; sum <= 4900; sum++) {
            if (dp[sum]) {
                minDiff = Math.min(minDiff, Math.abs(sum - target));
            }
        }

        return minDiff;
    }
}

// Time Complexity: O(m * n * max_sum) where m is number of rows, n is number of columns, and max_sum is the maximum possible sum (70*70=4900).
// Space Complexity: O(max_sum) for the boolean DP array.