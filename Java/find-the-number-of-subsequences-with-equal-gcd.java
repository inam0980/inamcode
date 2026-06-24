class Solution {
    private static final int MOD = 1_000_000_007;

    public int subsequencePairCount(int[] nums) {
        int maxVal = 0;
        for (int x : nums) {
            maxVal = Math.max(maxVal, x);
        }

        // counts[g] stores the number of elements in nums that are multiples of g.
        int[] counts = new int[maxVal + 1];
        for (int x : nums) {
            counts[x]++;
        }

        // pow2[k] stores 2^k % MOD.
        long[] pow2 = new long[nums.length + 1];
        pow2[0] = 1;
        for (int i = 1; i <= nums.length; i++) {
            pow2[i] = (pow2[i - 1] * 2) % MOD;
        }

        // dp[g] stores the number of pairs (seq1, seq2) such that GCD(seq1) = GCD(seq2) = g.
        long[] dp = new long[maxVal + 1];
        long totalPairs = 0;

        // Iterate g from maxVal down to 1.
        for (int g = maxVal; g >= 1; g--) {
            // numMultiplesG is the count of numbers in nums that are multiples of g.
            int numMultiplesG = 0;
            for (int i = g; i <= maxVal; i += g) {
                numMultiplesG += counts[i];
            }

            // Calculate the number of subsequences whose elements are all multiples of g.
            // This is 2^numMultiplesG - 1 (excluding the empty subsequence).
            long numSubsequencesOfMultiplesG = (pow2[numMultiplesG] - 1 + MOD) % MOD;

            // Calculate the number of pairs (seq1, seq2) where all elements are multiples of g.
            // If seq1 is chosen, the remaining elements (multiples of g) can form seq2.
            // The total number of ways to pick two disjoint non-empty subsequences from
            // numMultiplesG elements is (2^numMultiplesG - 1) * (2^numMultiplesG - 1)
            // if we consider them as independent choices.
            // However, we need to ensure they are disjoint.
            // The number of ways to pick a non-empty subsequence seq1 and a non-empty subsequence seq2
            // from numMultiplesG elements such that they are disjoint is:
            // (3^numMultiplesG - 2 * 2^numMultiplesG + 1)
            // This formula counts pairs (A, B) where A and B are disjoint and non-empty.
            // 3^N: each element can be in A, B, or neither.
            // - 2*2^N: subtract cases where A is empty or B is empty.
            // + 1: add back the case where both A and B are empty (subtracted twice).
            long waysToPickDisjointSubsequences = (pow2[numMultiplesG] * pow2[numMultiplesG]) % MOD; // 2^N * 2^N = 4^N
            waysToPickDisjointSubsequences = (pow2[numMultiplesG] * 3) % MOD; // This is wrong.
            // Correct: 3^N - 2*2^N + 1
            // 3^N is (1+2)^N = sum C(N,k) * 2^k.
            // Each of the numMultiplesG elements can be in seq1, seq2, or neither.
            // Let N = numMultiplesG.
            // Total ways to partition N elements into 3 groups (in seq1, in seq2, neither) is 3^N.
            // From these, subtract cases where seq1 is empty (2^N ways for elements to be in seq2 or neither).
            // Subtract cases where seq2 is empty (2^N ways for elements to be in seq1 or neither).
            // Add back case where both are empty (1 way).
            // So, (3^N - 2*2^N + 1) is the number of pairs (seq1, seq2) where seq1 and seq2 are disjoint and non-empty.
            // This is equivalent to (pow2[numMultiplesG] - 1)^2 - (pow2[numMultiplesG] - 1)
            // No, it's (pow2[numMultiplesG] - 1) * (pow2[numMultiplesG] - 1) - (pow2[numMultiplesG] - 1)
            // Let N = numMultiplesG.
            // Number of ways to choose seq1 (non-empty) from N elements: 2^N - 1.
            // Number of ways to choose seq2 (non-empty) from the remaining N - |seq1| elements: 2^(N-|seq1|) - 1.
            // This is complicated.
            // A simpler way:
            // For each element x that is a multiple of g:
            //   - it can be in seq1
            //   - it can be in seq2
            //   - it can be in neither
            // This gives 3^numMultiplesG total ways to assign elements to seq1, seq2, or neither.
            // From this, subtract cases where seq1 is empty: 2^numMultiplesG ways (elements can be in seq2 or neither).
            // Subtract cases where seq2 is empty: 2^numMultiplesG ways (elements can be in seq1 or neither).
            // Add back the case where both seq1 and seq2 are empty (which was subtracted twice): 1 way.
            // So, total ways for disjoint non-empty seq1, seq2 from elements that are multiples of g is:
            // (pow3[numMultiplesG] - 2 * pow2[numMultiplesG] + 1) % MOD.
            // We need pow3. Let's calculate it.
            long pow3_N = 1;
            for (int i = 0; i < numMultiplesG; i++) {
                pow3_N = (pow3_N * 3) % MOD;
            }

            long currentWays = (pow3_N - 2 * pow2[numMultiplesG] + 1 + 2 * MOD) % MOD; // Add 2*MOD to handle negative results

            dp[g] = currentWays;

            // Principle of Inclusion-Exclusion:
            // Subtract pairs where GCD is a multiple of g (e.g., 2g, 3g, ...)
            // These pairs have already been counted in dp[g] but their actual GCD is > g.
            for (int multiple = 2 * g; multiple <= maxVal; multiple += g) {
                dp[g] = (dp[g] - dp[multiple] + MOD) % MOD;
            }
            totalPairs = (totalPairs + dp[g]) % MOD;
        }

        return (int) totalPairs;
    }
}

// Time Complexity: O(MaxVal * log(MaxVal) + N * log(MaxVal))
// MaxVal is the maximum value in nums (200). N is nums.length (200).
// The outer loop runs MaxVal times (for g from MaxVal down to 1).
// Inside the outer loop:
//   - Calculating numMultiplesG: O(MaxVal / g) iterations. Sum over g: MaxVal * (1/1 + 1/2 + ... + 1/MaxVal) = O(MaxVal * log(MaxVal)).
//   - Calculating pow3_N: O(numMultiplesG) which is O(N) in worst case. Sum over g: O(N * MaxVal). This is too slow.
//     We can precompute pow3 similar to pow2.
//     Let's re-evaluate pow3_N calculation.
//     If pow3 is precomputed: O(1) for pow3_N.
//   - Inner loop for inclusion-exclusion: O(MaxVal / g) iterations. Sum over g: O(MaxVal * log(MaxVal)).
// Precomputing pow2: O(N).
// Precomputing pow3: O(N).
// Total time complexity: O(MaxVal * log(MaxVal) + N).
// Given constraints: MaxVal=200, N=200.
// 200 * log(200) approx 200 * 7.6 = 1520. This is efficient enough.

// Space Complexity: O(MaxVal + N)
// counts array: O(MaxVal)
// pow2 array: O(N)
// dp array: O(MaxVal)
// pow3 array (if precomputed): O(N)
// Total space complexity: O(MaxVal + N).