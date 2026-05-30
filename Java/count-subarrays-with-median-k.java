class Solution {
    public int countSubarrays(int[] nums, int k) {
        int n = nums.length;
        int kIndex = -1;

        // Find the index of k
        for (int i = 0; i < n; i++) {
            if (nums[i] == k) {
                kIndex = i;
                break;
            }
        }

        // Map to store counts of balance (num_greater - num_smaller) for subarrays ending at kIndex - 1
        // Key: balance, Value: count
        java.util.Map<Integer, Integer> prefixBalances = new java.util.HashMap<>();
        prefixBalances.put(0, 1); // Balance 0 for an empty prefix (before kIndex)

        int balance = 0;
        // Iterate from kIndex - 1 down to 0 to calculate balances for left subarrays
        for (int i = kIndex - 1; i >= 0; i--) {
            if (nums[i] > k) {
                balance++;
            } else { // nums[i] < k
                balance--;
            }
            prefixBalances.put(balance, prefixBalances.getOrDefault(balance, 0) + 1);
        }

        int ans = 0;
        balance = 0;
        // Iterate from kIndex up to n - 1 to calculate balances for right subarrays
        // and combine with left subarrays
        for (int i = kIndex; i < n; i++) {
            if (i > kIndex) { // Don't update balance for k itself, it's the median
                if (nums[i] > k) {
                    balance++;
                } else { // nums[i] < k
                    balance--;
                }
            }

            // For current right subarray ending at i, with balance 'balance':
            // We need a left subarray (or empty prefix) with balance 0 or 1
            // such that (left_balance + balance) is 0 or 1.
            // This means left_balance should be -balance or 1 - balance.
            ans += prefixBalances.getOrDefault(-balance, 0); // For total balance 0
            ans += prefixBalances.getOrDefault(1 - balance, 0); // For total balance 1
        }

        return ans;
    }
}

// Time Complexity: O(N)
// Space Complexity: O(N)