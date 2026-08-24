class Solution {
    public int stoneGameVIII(int[] stones) {
        int n = stones.length;
        
        // Calculate prefix sums for efficient range sum queries
        long[] prefixSum = new long[n];
        prefixSum[0] = stones[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + stones[i];
        }

        // dp[i] represents the maximum score difference achievable when the game starts
        // with the current leftmost stone being stones[i-1] (conceptually, after i-1 stones
        // have been processed and combined into a single stone at index 0, and the original
        // stones[i-1] is now the first stone of the remaining original array).
        // The actual state is when the current array is effectively stones[i-1...n-1]
        // and the first stone's value is prefixSum[i-1].
        // The game ends when only one stone is left, which means we need to consider
        // states where at least two stones are available for a move.
        // The last possible move is when the original array has 2 stones left.
        // This corresponds to dp[n-1] where the current array is effectively stones[n-2...n-1].
        // The base case for DP is when only two stones are left in the original array.
        // If the original array has stones[i-1], stones[i], ..., stones[n-1],
        // and the current leftmost stone has value S_i = prefixSum[i-1].
        // A player can choose x stones from the current array.
        // The current array effectively starts with S_i, followed by stones[i], ..., stones[n-1].
        // If a player chooses x stones, they remove S_i, stones[i], ..., stones[i+x-2].
        // The sum added to score is S_i + stones[i] + ... + stones[i+x-2] = prefixSum[i+x-2].
        // The new stone value is also prefixSum[i+x-2].
        // The remaining game state starts with this new stone and stones[i+x-1], ..., stones[n-1].
        // This is equivalent to starting a new game with the original array effectively
        // starting from index i+x-1, and the first stone having value prefixSum[i+x-2].

        // Let dp[i] be the maximum score difference when the current game state
        // is equivalent to having the first stone's value as prefixSum[i]
        // and the remaining stones are stones[i+1], ..., stones[n-1].
        // The game starts with the first stone having value prefixSum[0]
        // and remaining stones stones[1], ..., stones[n-1].
        // So we are interested in dp[0].

        // The state dp[i] represents the maximum score difference if the current
        // leftmost stone has value `current_sum = prefixSum[i]`, and the remaining
        // stones are `stones[i+1], ..., stones[n-1]`.
        // A player can choose to remove `x` stones, where `x > 1`.
        // The available stones are `current_sum`, `stones[i+1]`, ..., `stones[n-1]`.
        // If `x` stones are chosen, the sum removed is `current_sum + stones[i+1] + ... + stones[i+x-1]`.
        // This sum is `prefixSum[i+x-1]`.
        // The player's score increases by `prefixSum[i+x-1]`.
        // The new game state starts with a stone of value `prefixSum[i+x-1]`
        // and remaining stones `stones[i+x], ..., stones[n-1]`.
        // This is exactly the state `dp[i+x-1]`.
        // So, `dp[i] = max_{x=2 to n-i} (prefixSum[i+x-1] - dp[i+x-1])`.
        // The index `i+x-1` ranges from `i+1` to `n-1`.
        // So, `dp[i] = max_{j=i+1 to n-1} (prefixSum[j] - dp[j])`.

        // Base case: When only two stones are left in the effective array.
        // This means the current state is `prefixSum[n-2]` and `stones[n-1]`.
        // The only move is to take both stones.
        // The score added is `prefixSum[n-1]`.
        // The game ends. So `dp[n-2] = prefixSum[n-1]`.
        // This is because the opponent has no moves left, so their score contribution is 0.

        long[] dp = new long[n];
        
        // Base case: When only two stones are left in the conceptual array.
        // This means the current array is effectively `[prefixSum[n-2], stones[n-1]]`.
        // The only move is to take both. The sum is `prefixSum[n-1]`.
        // This is the final score for the current player.
        dp[n - 1] = prefixSum[n - 1]; 

        // Iterate backwards from n-2 down to 0
        // dp[i] = max_{j=i+1 to n-1} (prefixSum[j] - dp[j])
        // To optimize the max calculation, we can maintain a suffix maximum.
        // Let max_suffix_diff[i] = max_{k=i to n-1} (prefixSum[k] - dp[k]).
        // Then dp[i] = max_suffix_diff[i+1].

        long maxSuffixDiff = prefixSum[n - 1] - dp[n - 1]; // For j = n-1
        
        for (int i = n - 2; i >= 0; i--) {
            // dp[i] is the maximum score difference if the current player makes a move
            // from state i. The player chooses to remove stones up to index j (original index).
            // The score obtained is prefixSum[j]. The opponent then plays from state j,
            // resulting in a difference of dp[j]. So the current player's score difference
            // is prefixSum[j] - dp[j].
            // The player wants to maximize this.
            // The possible values for j are from i+1 to n-1.
            dp[i] = maxSuffixDiff;
            
            // Update maxSuffixDiff for the next iteration (i-1)
            // maxSuffixDiff for i-1 will include prefixSum[i] - dp[i]
            maxSuffixDiff = Math.max(maxSuffixDiff, prefixSum[i] - dp[i]);
        }
        
        return (int) dp[0];
    }
}
// Time complexity: O(N) due to single pass for prefix sums and single pass for DP.
// Space complexity: O(N) for prefix sums array and DP array.