class Solution {
    public int zigZagArrays(int n, int l, int r) {
        int MOD = 1_000_000_007;
        int K = r - l + 1; // Range size

        // Base cases for n=1 and n=2
        if (n == 1) {
            return K;
        }
        if (n == 2) {
            return (int) ((long) K * (K - 1) % MOD);
        }

        // dp[i][j][0] = count of valid arrays of length i ending with j, where j > prev_element
        // dp[i][j][1] = count of valid arrays of length i ending with j, where j < prev_element
        long[][] dp = new long[K + 1][2];

        // Initialize dp for length 2
        // For each possible last element `j` (from 1 to K)
        // If j is greater than previous element (prev_element < j), there are (j-1) choices for prev_element.
        // If j is smaller than previous element (prev_element > j), there are (K-j) choices for prev_element.
        for (int j = 1; j <= K; j++) {
            dp[j][0] = (j - 1); // prev_element < j
            dp[j][1] = (K - j); // prev_element > j
        }

        // If n is large, we need matrix exponentiation.
        // The state transition can be represented as a matrix multiplication.
        // The state vector for length `i` is a (2K)-dimensional vector:
        // [dp[1][0], dp[1][1], dp[2][0], dp[2][1], ..., dp[K][0], dp[K][1]]
        // Let's define the transition matrix.
        // new_dp[j][0] = sum(dp[p][1]) for all p != j
        // new_dp[j][1] = sum(dp[p][0]) for all p != j

        // Calculate total sums for previous length
        long sum_dp_prev_0 = 0; // sum of dp[p][0] for all p
        long sum_dp_prev_1 = 0; // sum of dp[p][1] for all p
        for (int p = 1; p <= K; p++) {
            sum_dp_prev_0 = (sum_dp_prev_0 + dp[p][0]) % MOD;
            sum_dp_prev_1 = (sum_dp_prev_1 + dp[p][1]) % MOD;
        }

        // The transition for dp[j][0] (ending with j, j > prev)
        // is sum of dp[p][1] (ending with p, p < prev) for all p != j.
        // This is (sum_dp_prev_1 - dp[j][1])
        // The transition for dp[j][1] (ending with j, j < prev)
        // is sum of dp[p][0] (ending with p, p > prev) for all p != j.
        // This is (sum_dp_prev_0 - dp[j][0])

        // Construct the (2K) x (2K) transition matrix.
        // The state vector is [dp[1][0], dp[1][1], ..., dp[K][0], dp[K][1]]
        // Index mapping: (val, type) -> (val-1)*2 + type
        // type 0: > prev, type 1: < prev
        int matrix_size = 2 * K;
        long[][] T = new long[matrix_size][matrix_size];

        for (int j = 1; j <= K; j++) { // current element value
            int current_idx_0 = (j - 1) * 2;     // index for dp[j][0]
            int current_idx_1 = (j - 1) * 2 + 1; // index for dp[j][1]

            // To calculate new_dp[j][0] (ending with j, j > prev):
            // It depends on dp[p][1] for all p != j.
            // So, for each p from 1 to K, if p != j, add 1 to T[current_idx_0][(p-1)*2 + 1]
            for (int p = 1; p <= K; p++) {
                if (p != j) {
                    T[current_idx_0][(p - 1) * 2 + 1] = 1;
                }
            }

            // To calculate new_dp[j][1] (ending with j, j < prev):
            // It depends on dp[p][0] for all p != j.
            // So, for each p from 1 to K, if p != j, add 1 to T[current_idx_1][(p-1)*2]
            for (int p = 1; p <= K; p++) {
                if (p != j) {
                    T[current_idx_1][(p - 1) * 2] = 1;
                }
            }
        }

        // Raise T to the power of (n-2)
        long[][] T_pow_n_minus_2 = matrixPower(T, n - 2, MOD);

        // Initial state vector (for length 2)
        long[] initial_state = new long[matrix_size];
        for (int j = 1; j <= K; j++) {
            initial_state[(j - 1) * 2] = dp[j][0];
            initial_state[(j - 1) * 2 + 1] = dp[j][1];
        }

        // Multiply T_pow_n_minus_2 by initial_state to get the state for length n
        long[] final_state = new long[matrix_size];
        for (int i = 0; i < matrix_size; i++) {
            for (int j = 0; j < matrix_size; j++) {
                final_state[i] = (final_state[i] + T_pow_n_minus_2[i][j] * initial_state[j]) % MOD;
            }
        }

        // Sum all elements in final_state to get the total count
        long total_count = 0;
        for (int i = 0; i < matrix_size; i++) {
            total_count = (total_count + final_state[i]) % MOD;
        }

        return (int) total_count;
    }

    // Helper function for matrix multiplication
    private long[][] multiplyMatrices(long[][] A, long[][] B, int MOD) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        long[][] C = new long[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % MOD;
                }
            }
        }
        return C;
    }

    // Helper function for matrix exponentiation (binary exponentiation)
    private long[][] matrixPower(long[][] base, long exp, int MOD) {
        int size = base.length;
        long[][] result = new long[size][size];
        // Initialize result as identity matrix
        for (int i = 0; i < size; i++) {
            result[i][i] = 1;
        }

        while (exp > 0) {
            if (exp % 2 == 1) {
                result = multiplyMatrices(result, base, MOD);
            }
            base = multiplyMatrices(base, base, MOD);
            exp /= 2;
        }
        return result;
    }
}

// Time Complexity: O((r-l+1)^3 * log n)
// Space Complexity: O((r-l+1)^2)