class Solution {
    private long[][][][] dp;
    private int[] digits;

    public long totalWaviness(long num1, long num2) {
        return solve(num2) - solve(num1 - 1);
    }

    private long solve(long n) {
        if (n < 100) return 0; // Numbers less than 3 digits have waviness 0

        String s = String.valueOf(n);
        digits = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            digits[i] = s.charAt(i) - '0';
        }

        // dp[idx][prev1][prev2][isLess][isLeadingZero]
        // prev1: digit at idx-1
        // prev2: digit at idx-2
        // isLess: boolean, true if current prefix is already less than N's prefix
        // isLeadingZero: boolean, true if current prefix is all zeros
        dp = new long[digits.length][11][11][2][2];
        for (int i = 0; i < digits.length; i++) {
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 11; k++) {
                    for (int l = 0; l < 2; l++) {
                        for (int m = 0; m < 2; m++) {
                            dp[i][j][k][l][m] = -1;
                        }
                    }
                }
            }
        }

        // Initial call: idx=0, prev1=10 (dummy), prev2=10 (dummy), isLess=false, isLeadingZero=true
        return digitDP(0, 10, 10, false, true);
    }

    private long digitDP(int idx, int prev1, int prev2, boolean isLess, boolean isLeadingZero) {
        if (idx == digits.length) {
            return 0; // Base case: reached end of number, no more waviness to add
        }

        if (dp[idx][prev1][prev2][isLess ? 1 : 0][isLeadingZero ? 1 : 0] != -1) {
            return dp[idx][prev1][prev2][isLess ? 1 : 0][isLeadingZero ? 1 : 0];
        }

        long totalWaviness = 0;
        int upperLimit = isLess ? 9 : digits[idx];

        for (int d = 0; d <= upperLimit; d++) {
            boolean newIsLess = isLess || (d < upperLimit);
            boolean newIsLeadingZero = isLeadingZero && (d == 0);

            // Calculate waviness for current digit 'd'
            long currentWaviness = 0;
            if (!newIsLeadingZero && idx >= 2) { // Only consider digits from index 2 onwards for peaks/valleys
                if (prev1 != 10 && prev2 != 10) { // Ensure prev1 and prev2 are valid digits
                    if (prev1 > prev2 && prev1 > d) { // Peak
                        currentWaviness = 1;
                    } else if (prev1 < prev2 && prev1 < d) { // Valley
                        currentWaviness = 1;
                    }
                }
            }
            
            // Add current waviness and recurse for next digit
            totalWaviness += currentWaviness + digitDP(idx + 1, d, prev1, newIsLess, newIsLeadingZero);
        }

        return dp[idx][prev1][prev2][isLess ? 1 : 0][isLeadingZero ? 1 : 0] = totalWaviness;
    }
}
// Time Complexity: O(logN * 10 * 10 * 2 * 2 * 10) = O(logN) where N is the maximum value of num2 (10^15)
// Space Complexity: O(logN * 10 * 10 * 2 * 2) = O(logN) for DP table