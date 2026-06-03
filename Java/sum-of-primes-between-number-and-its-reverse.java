class Solution {
    public int sumOfPrimesInRange(int n) {
        // Calculate the reverse of n
        int r = 0;
        int tempN = n;
        while (tempN > 0) {
            r = r * 10 + tempN % 10;
            tempN /= 10;
        }

        // Determine the range [minVal, maxVal]
        int minVal = Math.min(n, r);
        int maxVal = Math.max(n, r);

        // Sieve of Eratosthenes to find primes up to maxVal
        // Max possible value for n is 1000, so maxVal is at most 1000.
        // We need to check primes up to maxVal.
        boolean[] isPrime = new boolean[maxVal + 1];
        if (maxVal >= 2) {
            for (int i = 2; i <= maxVal; i++) {
                isPrime[i] = true; // Assume all numbers are prime initially
            }
        }

        for (int p = 2; p * p <= maxVal; p++) {
            if (isPrime[p]) {
                // Mark multiples of p as not prime
                for (int i = p * p; i <= maxVal; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        // Sum primes within the range [minVal, maxVal]
        int sum = 0;
        for (int i = minVal; i <= maxVal; i++) {
            if (isPrime[i]) {
                sum += i;
            }
        }

        return sum;
    }
}
// Time Complexity: O(max(n, r) * log(log(max(n, r)))) due to Sieve of Eratosthenes.
// Space Complexity: O(max(n, r)) for the boolean array.