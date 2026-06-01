class Solution {
    public int maxValueOfCoins(List<List<Integer>> piles, int k) {
        int n = piles.size();
        // dp[j] stores the maximum value for j coins considering piles processed so far
        int[] dp = new int[k + 1];

        // Iterate through each pile
        for (int i = 0; i < n; i++) {
            List<Integer> currentPile = piles.get(i);
            int pileSize = currentPile.size();
            
            // Calculate prefix sums for the current pile to quickly get sum of top 'x' coins
            int[] prefixSum = new int[pileSize + 1];
            for (int j = 0; j < pileSize; j++) {
                prefixSum[j + 1] = prefixSum[j] + currentPile.get(j);
            }

            // Iterate backwards from k down to 1
            // This ensures that dp[j - coinsTaken] uses values from the previous pile
            for (int j = k; j >= 0; j--) {
                // Iterate through possible number of coins to take from the current pile
                for (int coinsTaken = 0; coinsTaken <= pileSize; coinsTaken++) {
                    // If we have enough capacity (j) to take 'coinsTaken' from current pile
                    if (j - coinsTaken >= 0) {
                        // Update dp[j] with the maximum value
                        // This is max of:
                        // 1. Current dp[j] (value without considering current pile, or with previous configurations)
                        // 2. Value from previous piles (dp[j - coinsTaken]) + value of 'coinsTaken' from current pile
                        dp[j] = Math.max(dp[j], dp[j - coinsTaken] + prefixSum[coinsTaken]);
                    }
                }
            }
        }

        return dp[k];
    }
}
// Time Complexity: O(N * K * MaxPileSize) where N is the number of piles, K is the target number of coins, and MaxPileSize is the maximum number of coins in any pile.
// Space Complexity: O(K) for the DP array.