class Solution {
    public int largestPrime(int n) {
        // Sieve of Eratosthenes to find all primes up to n
        boolean[] isPrime = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }
        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= n; i += p)
                    isPrime[i] = false;
            }
        }

        // Store primes in a list for easy iteration
        java.util.List<Integer> primes = new java.util.ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        int maxPrimeSum = 0;
        long currentSum = 0; // Use long to prevent overflow during sum calculation

        // Iterate through primes to find consecutive sums starting from 2
        for (int prime : primes) {
            currentSum += prime;
            if (currentSum > n) {
                break; // Sum exceeds n, no need to add more primes
            }
            // Check if the current sum is prime and within bounds
            if (currentSum <= n && isPrime[(int) currentSum]) {
                maxPrimeSum = (int) currentSum;
            }
        }

        return maxPrimeSum;
    }
}
// Time Complexity: O(N log log N) due to Sieve of Eratosthenes, plus O(sqrt(N)) for prime list iteration. Overall O(N log log N).
// Space Complexity: O(N) for the boolean array and the list of primes.