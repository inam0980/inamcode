class Solution {
    public long gcdSum(int[] nums) {
        int n = nums.length;
        int[] prefixGcd = new int[n];
        int currentMax = 0; // Tracks max(nums[0]...nums[i])

        // Step 1: Construct prefixGcd array
        for (int i = 0; i < n; i++) {
            currentMax = Math.max(currentMax, nums[i]);
            prefixGcd[i] = gcd(nums[i], currentMax);
        }

        // Step 2: Sort prefixGcd in non-decreasing order
        java.util.Arrays.sort(prefixGcd);

        // Step 3 & 4: Form pairs and sum their GCDs
        long totalGcdSum = 0;
        int left = 0;
        int right = n - 1;

        // Pair smallest with largest until left pointer crosses right pointer
        while (left < right) {
            totalGcdSum += gcd(prefixGcd[left], prefixGcd[right]);
            left++;
            right--;
        }

        return totalGcdSum;
    }

    // Helper function to compute GCD using Euclidean algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}

// Time complexity: O(N log N) due to sorting. GCD computations take O(log(max_val)).
// Space complexity: O(N) for storing prefixGcd array.