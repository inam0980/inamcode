class Solution {
    public int zigZagArrays(int n, int l, int r) {
        if (n == 0) return 0;
        if (n == 1) return r - l + 1;
        
        long MOD = 1_000_000_007;
        int K = r - l + 1; // Number of possible values
        
        // dp[i][j][0] = number of valid arrays of length i ending with j, where j > prev_element
        // dp[i][j][1] = number of valid arrays of length i ending with j, where j < prev_element
        long[][][] dp = new long[n + 1][K + 1][2];
        
        // Base case: arrays of length 2
        // For each possible ending value j (from 1 to K)
        // For each possible previous value prev_j (from 1 to K)
        // If prev_j != j, then [prev_j, j] is a valid length 2 array.
        // We need to sum up counts for dp[2][j][0] and dp[2][j][1]
        for (int j = 1; j <= K; j++) { // current value
            for (int prev_j = 1; prev_j <= K; prev_j++) { // previous value
                if (prev_j == j) continue;
                if (j > prev_j) { // current > previous, increasing
                    dp[2][j][0] = (dp[2][j][0] + 1) % MOD;
                } else { // current < previous, decreasing
                    dp[2][j][1] = (dp[2][j][1] + 1) % MOD;
                }
            }
        }
        
        // Fill DP table for length 3 to n
        for (int i = 3; i <= n; i++) {
            for (int j = 1; j <= K; j++) { // current element value (j-th smallest value)
                // Calculate dp[i][j][0] (ending with j, j > prev_element)
                // The previous state must be dp[i-1][prev_j][1] (prev_j < prev_prev_element)
                // And we need j > prev_j
                for (int prev_j = 1; prev_j < j; prev_j++) { // prev_j must be less than j
                    dp[i][j][0] = (dp[i][j][0] + dp[i-1][prev_j][1]) % MOD;
                }
                
                // Calculate dp[i][j][1] (ending with j, j < prev_element)
                // The previous state must be dp[i-1][prev_j][0] (prev_j > prev_prev_element)
                // And we need j < prev_j
                for (int prev_j = j + 1; prev_j <= K; prev_j++) { // prev_j must be greater than j
                    dp[i][j][1] = (dp[i][j][1] + dp[i-1][prev_j][0]) % MOD;
                }
            }
        }
        
        // Sum up all valid arrays of length n
        long totalCount = 0;
        for (int j = 1; j <= K; j++) {
            totalCount = (totalCount + dp[n][j][0]) % MOD;
            totalCount = (totalCount + dp[n][j][1]) % MOD;
        }
        
        return (int) totalCount;
    }
}
// Time Complexity: O(n * K^2) where K = r - l + 1
// Space Complexity: O(n * K)