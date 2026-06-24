class Solution {
    public int zigZagArrays(int n, int l, int r) {
        int MOD = 1_000_000_007;
        int K = r - l + 1; // Range size

        // dp[i][j][0] = number of zigzag arrays of length i ending with j, where j > prev
        // dp[i][j][1] = number of zigzag arrays of length i ending with j, where j < prev
        // dp[i][j][2] = number of zigzag arrays of length i ending with j, where j == prev (not allowed for length >= 2)
        // For length 1, any value is valid.
        // For length 2, any two distinct values are valid.
        // For length 3 onwards, the zigzag condition applies.

        // Base cases for n=1 and n=2
        if (n == 1) {
            return K;
        }
        if (n == 2) {
            return (int) ((long) K * (K - 1) % MOD);
        }

        // dp[val][dir] stores counts for current length
        // val is the actual value (0 to K-1, mapping l to r)
        // dir: 0 for increasing (prev < current), 1 for decreasing (prev > current)
        long[][] dp = new long[K][2];

        // Initialize for length 2
        // For each value `curr` (0 to K-1), it can be preceded by any `prev` != `curr`.
        // If `curr` > `prev`, it's an increasing sequence.
        // If `curr` < `prev`, it's a decreasing sequence.
        for (int curr = 0; curr < K; curr++) {
            for (int prev = 0; prev < K; prev++) {
                if (curr == prev) continue;
                if (curr > prev) {
                    dp[curr][0]++; // curr is greater than prev
                } else { // curr < prev
                    dp[curr][1]++; // curr is smaller than prev
                }
            }
        }

        // Use matrix exponentiation for n > 2
        // The state vector for matrix multiplication will be of size 2*K
        // [dp[0][0], dp[0][1], dp[1][0], dp[1][1], ..., dp[K-1][0], dp[K-1][1]]
        // This represents the counts for length `i`.
        // We want to find the counts for length `n`.

        // Transition matrix T of size (2K x 2K)
        // T[ (prev_val*2 + prev_dir) ][ (curr_val*2 + curr_dir) ]
        // T[j][i] means transition from state j to state i
        // Let's define states as (value, direction):
        // State (v, 0) means ending with value v, previous was smaller (v > prev)
        // State (v, 1) means ending with value v, previous was larger (v < prev)

        // To calculate dp[curr][0] (curr > prev):
        // Sum of dp[prev][1] for all prev < curr
        // (prev was larger than prev_prev, now curr > prev, so prev_prev < prev < curr)
        // To calculate dp[curr][1] (curr < prev):
        // Sum of dp[prev][0] for all prev > curr
        // (prev was smaller than prev_prev, now curr < prev, so prev_prev > prev > curr)

        // Let's build the transition matrix M.
        // M[row_idx][col_idx]
        // row_idx corresponds to (prev_val, prev_dir)
        // col_idx corresponds to (curr_val, curr_dir)
        // row_idx = prev_val * 2 + prev_dir
        // col_idx = curr_val * 2 + curr_dir
        int matrixSize = 2 * K;
        long[][] M = new long[matrixSize][matrixSize];

        for (int prev_val = 0; prev_val < K; prev_val++) {
            for (int curr_val = 0; curr_val < K; curr_val++) {
                if (prev_val == curr_val) continue; // No two adjacent elements are equal

                // Case 1: curr_val > prev_val (current sequence ends with increasing step)
                // This means curr_dir = 0.
                // The previous step must have been decreasing (prev_dir = 1) to satisfy zigzag.
                // So, we transition from (prev_val, 1) to (curr_val, 0).
                if (curr_val > prev_val) {
                    int row = prev_val * 2 + 1; // prev_dir = 1 (decreasing)
                    int col = curr_val * 2 + 0; // curr_dir = 0 (increasing)
                    M[row][col] = 1;
                }

                // Case 2: curr_val < prev_val (current sequence ends with decreasing step)
                // This means curr_dir = 1.
                // The previous step must have been increasing (prev_dir = 0) to satisfy zigzag.
                // So, we transition from (prev_val, 0) to (curr_val, 1).
                if (curr_val < prev_val) {
                    int row = prev_val * 2 + 0; // prev_dir = 0 (increasing)
                    int col = curr_val * 2 + 1; // curr_dir = 1 (decreasing)
                    M[row][col] = 1;
                }
            }
        }

        // Initial state vector (column vector) for length 2
        long[] initialVector = new long[matrixSize];
        for (int i = 0; i < K; i++) {
            initialVector[i * 2 + 0] = dp[i][0]; // value i, increasing
            initialVector[i * 2 + 1] = dp[i][1]; // value i, decreasing
        }

        // Compute M^(n-2)
        long[][] M_pow_n_minus_2 = matrixPower(M, n - 2, MOD);

        // Multiply initialVector by M_pow_n_minus_2
        long[] finalVector = new long[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                finalVector[i] = (finalVector[i] + M_pow_n_minus_2[i][j] * initialVector[j]) % MOD;
            }
        }

        // Sum all elements in finalVector to get the total count
        long totalCount = 0;
        for (long count : finalVector) {
            totalCount = (totalCount + count) % MOD;
        }

        return (int) totalCount;
    }

    // Helper function for matrix multiplication
    private long[][] multiplyMatrices(long[][] A, long[][] B, int MOD) {
        int size = A.length;
        long[][] C = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % MOD;
                }
            }
        }
        return C;
    }

    // Helper function for matrix exponentiation (binary exponentiation)
    private long[][] matrixPower(long[][] base, int exp, int MOD) {
        int size = base.length;
        long[][] result = new long[size][size];
        // Initialize result as identity matrix
        for (int i = 0; i < size; i++) {
            result[i][i] = 1;
        }

        long[][] currentBase = base;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = multiplyMatrices(result, currentBase, MOD);
            }
            currentBase = multiplyMatrices(currentBase, currentBase, MOD);
            exp /= 2;
        }
        return result;
    }
}

// Time complexity: O((r-l+1)^3 * log n)
// Space complexity: O((r-l+1)^2)