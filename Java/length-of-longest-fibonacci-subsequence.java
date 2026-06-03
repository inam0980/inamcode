class Solution {
    public int lenLongestFibSubseq(int[] arr) {
        int n = arr.length;
        // Map to store value -> index for O(1) lookup
        java.util.Map<Integer, Integer> valToIndex = new java.util.HashMap<>();
        for (int i = 0; i < n; i++) {
            valToIndex.put(arr[i], i);
        }

        // dp[j][i] stores the length of the longest Fibonacci-like subsequence ending with arr[j] and arr[i]
        // where arr[j] is the second to last element and arr[i] is the last element.
        // The length is at least 2 (arr[j], arr[i]).
        int[][] dp = new int[n][n];
        int maxLen = 0;

        // Iterate through all possible pairs (arr[j], arr[i]) where j < i
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Calculate the required previous element 'k'
                int k = arr[i] - arr[j];

                // If k is less than arr[j], it means arr[k_idx] < arr[j]
                // And k must exist in the array and its index k_idx must be less than j
                if (k < arr[j] && valToIndex.containsKey(k)) {
                    int k_idx = valToIndex.get(k);
                    // If k_idx is valid (less than j), we can extend the subsequence
                    if (k_idx < j) {
                        // The new length is dp[k_idx][j] + 1.
                        // If dp[k_idx][j] was 0 (meaning no fib seq ending with k_idx, j found yet),
                        // it implies (k_idx, j) is the start of a new fib seq of length 2.
                        // So, we initialize dp[j][i] to 2 + 1 = 3.
                        // Otherwise, we extend the existing sequence.
                        dp[j][i] = dp[k_idx][j] + 1;
                    }
                }
                
                // If dp[j][i] is still 0 after the above check, it means (arr[j], arr[i])
                // cannot extend any existing Fibonacci-like subsequence.
                // However, (arr[j], arr[i]) itself forms a Fibonacci-like subsequence of length 2.
                // For our DP state, we consider the base length to be 2 for any pair (arr[j], arr[i]).
                // If dp[j][i] is non-zero, it means we found a sequence of length 3 or more.
                // We need to ensure that any sequence of length 3 or more is correctly captured.
                // The minimum length for a Fibonacci-like subsequence is 3.
                // So, if dp[j][i] is greater than 0, it means we found a sequence of length at least 3.
                if (dp[j][i] > 0) {
                    maxLen = Math.max(maxLen, dp[j][i]);
                } else {
                    // This handles the case where (arr[j], arr[i]) is the first two elements
                    // of a potential Fibonacci-like subsequence.
                    // We don't explicitly set dp[j][i] = 2 here, because we only care about
                    // sequences of length >= 3 for the final answer.
                    // The dp[j][i] value will be 0 if no 'k' was found, and that's fine.
                    // The maxLen will only update if dp[j][i] becomes >= 3.
                }
            }
        }

        // If maxLen is 0, it means no Fibonacci-like subsequence of length >= 3 was found.
        // The problem asks to return 0 in that case.
        // Otherwise, return maxLen.
        return maxLen;
    }
}
// Time Complexity: O(N^2) due to nested loops. Map lookups are O(1) on average.
// Space Complexity: O(N^2) for the DP table and O(N) for the hash map.