class Solution {
    public long countMajoritySubarrays(int[] nums, int target) {
        long totalCount = 0;
        int n = nums.length;
        
        // Find the first occurrence of target to split the problem
        int firstTargetIndex = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == target) {
                firstTargetIndex = i;
                break;
            }
        }

        // If target is not in nums, no subarray can have it as majority
        if (firstTargetIndex == -1) {
            return 0;
        }

        // Map to store counts of balance values for subarrays ending before firstTargetIndex
        // balance = count(target) - count(non-target)
        // We need to store counts for balance values when iterating from right to left
        // from firstTargetIndex - 1 down to 0.
        // The balance values can range from -(firstTargetIndex) to 0.
        // To use an array, we offset by firstTargetIndex.
        int[] leftBalanceCounts = new int[firstTargetIndex + 1];
        leftBalanceCounts[firstTargetIndex] = 1; // Balance 0 at index firstTargetIndex - 1 (empty prefix)
        int currentBalance = 0; // Balance for elements from firstTargetIndex - 1 down to i
        for (int i = firstTargetIndex - 1; i >= 0; i--) {
            if (nums[i] == target) {
                currentBalance++;
            } else {
                currentBalance--;
            }
            // Store count for this balance. Offset by firstTargetIndex to map to array index.
            // A balance of 'b' corresponds to index 'firstTargetIndex + b'.
            // Since currentBalance is <= 0 here, firstTargetIndex + currentBalance is a valid index.
            leftBalanceCounts[firstTargetIndex + currentBalance]++;
        }

        // Iterate from firstTargetIndex to n-1, considering each i as the right endpoint
        // of a subarray.
        // For each i, we want to find j <= i such that target is majority in nums[j..i].
        // This means count(target in nums[j..i]) > count(non-target in nums[j..i]).
        // Let balance_k = count(target in nums[k..i]) - count(non-target in nums[k..i]).
        // We need balance_k > 0.
        //
        // We can rewrite this as:
        // (count(target in nums[0..i]) - count(non-target in nums[0..i])) -
        // (count(target in nums[0..j-1]) - count(non-target in nums[0..j-1])) > 0
        //
        // Let prefixBalance[k] = count(target in nums[0..k]) - count(non-target in nums[0..k]).
        // We need prefixBalance[i] - prefixBalance[j-1] > 0.
        //
        // For subarrays starting at or after firstTargetIndex:
        // Iterate i from firstTargetIndex to n-1.
        // Maintain currentBalance for nums[firstTargetIndex..i].
        // For each i, we need to find j such that nums[j..i] has target as majority.
        //
        // Case 1: Subarrays nums[j..i] where j <= firstTargetIndex.
        // For a fixed i >= firstTargetIndex, we need to find j <= firstTargetIndex such that
        // balance(nums[j..i]) > 0.
        // balance(nums[j..i]) = balance(nums[j..firstTargetIndex-1]) + balance(nums[firstTargetIndex..i]).
        // Let balance_prefix_left = balance(nums[j..firstTargetIndex-1])
        // Let balance_suffix_right = balance(nums[firstTargetIndex..i])
        // We need balance_prefix_left + balance_suffix_right > 0.
        // This means balance_prefix_left > -balance_suffix_right.
        //
        // When iterating i from firstTargetIndex to n-1:
        // currentBalanceFromTarget = balance(nums[firstTargetIndex..i])
        // For each i, we need to count j <= firstTargetIndex such that
        // balance(nums[j..firstTargetIndex-1]) > -currentBalanceFromTarget.
        // The balance(nums[j..firstTargetIndex-1]) values are stored in leftBalanceCounts.
        // These balances are relative to firstTargetIndex-1.
        // A balance 'b' in leftBalanceCounts corresponds to balance(nums[k..firstTargetIndex-1]) where k is some index.
        // The index in leftBalanceCounts is firstTargetIndex + b.
        // We need to sum up counts for all b such that b > -currentBalanceFromTarget.
        //
        // Case 2: Subarrays nums[j..i] where j > firstTargetIndex.
        // This is a standard prefix sum problem.
        // Let prefixBalance[k] = count(target in nums[firstTargetIndex..k]) - count(non-target in nums[firstTargetIndex..k]).
        // We need prefixBalance[i] - prefixBalance[j-1] > 0.
        // This is equivalent to prefixBalance[j-1] < prefixBalance[i].
        // We can use a frequency map for prefixBalance values encountered so far.

        // currentBalanceFromTarget: balance for subarray nums[firstTargetIndex...i]
        int currentBalanceFromTarget = 0; 
        // Map to store counts of prefix balances for subarrays starting at or after firstTargetIndex.
        // Key: balance value, Value: count of times this balance occurred.
        // We need to store counts for prefixBalance[k] where k >= firstTargetIndex - 1.
        // prefixBalance[firstTargetIndex - 1] is 0.
        // The balances can range from -(i - firstTargetIndex + 1) to (i - firstTargetIndex + 1).
        // To use an array, we offset by n.
        int[] rightBalanceCounts = new int[2 * n + 1]; // Offset by n
        rightBalanceCounts[n] = 1; // For prefixBalance[firstTargetIndex - 1] = 0

        for (int i = firstTargetIndex; i < n; i++) {
            if (nums[i] == target) {
                currentBalanceFromTarget++;
            } else {
                currentBalanceFromTarget--;
            }

            // Case 1: Subarrays nums[j..i] where j <= firstTargetIndex.
            // We need balance(nums[j..firstTargetIndex-1]) > -currentBalanceFromTarget.
            // Iterate through possible balances 'b' from leftBalanceCounts.
            // 'b' ranges from -(firstTargetIndex) to 0.
            // We need b > -currentBalanceFromTarget.
            // The smallest possible balance is -(firstTargetIndex).
            // The largest possible balance is 0.
            // The threshold for 'b' is -currentBalanceFromTarget.
            // We need to sum counts for b from floor(-currentBalanceFromTarget) + 1 up to 0.
            
            // The index in leftBalanceCounts is firstTargetIndex + b.
            // We need to sum counts from index firstTargetIndex + (floor(-currentBalanceFromTarget) + 1)
            // up to firstTargetIndex + 0.
            
            // The minimum balance we need to consider is -firstTargetIndex.
            // The maximum balance we need to consider is 0.
            // The lower bound for 'b' is max(-firstTargetIndex, floor(-currentBalanceFromTarget) + 1).
            // The upper bound for 'b' is 0.
            
            // Iterate 'b' from 0 down to -firstTargetIndex.
            // If b > -currentBalanceFromTarget, add its count.
            for (int b = 0; b >= -firstTargetIndex; b--) {
                if (b > -currentBalanceFromTarget) {
                    totalCount += leftBalanceCounts[firstTargetIndex + b];
                } else {
                    // Since b is decreasing, if this b is not greater,
                    // no smaller b will be either.
                    break;
                }
            }

            // Case 2: Subarrays nums[j..i] where j > firstTargetIndex.
            // We need prefixBalance[j-1] < currentBalanceFromTarget.
            // Here, prefixBalance[k] is balance(nums[firstTargetIndex..k]).
            // We need to count j-1 from firstTargetIndex-1 up to i-1 such that
            // balance(nums[firstTargetIndex..j-1]) < currentBalanceFromTarget.
            // The balances are stored in rightBalanceCounts.
            // The index in rightBalanceCounts is n + balance.
            // We need to sum counts for balance 'b_r' such that b_r < currentBalanceFromTarget.
            // Iterate 'b_r' from currentBalanceFromTarget - 1 down to -(i - firstTargetIndex).
            // The smallest possible balance is -(i - firstTargetIndex).
            // The largest possible balance is currentBalanceFromTarget - 1.
            
            // Sum counts for b_r from -(i - firstTargetIndex) up to currentBalanceFromTarget - 1.
            // The actual range of balances is from -(i - firstTargetIndex) to currentBalanceFromTarget.
            // We need to sum counts for balances strictly less than currentBalanceFromTarget.
            // Iterate from the smallest possible balance up to currentBalanceFromTarget - 1.
            for (int b_r = -(i - firstTargetIndex); b_r < currentBalanceFromTarget; b_r++) {
                totalCount += rightBalanceCounts[n + b_r];
            }
            
            // Add currentBalanceFromTarget to rightBalanceCounts for future iterations
            rightBalanceCounts[n + currentBalanceFromTarget]++;
        }

        return totalCount;
    }
}

// Time Complexity: O(N^2) in worst case.
// The nested loops for calculating totalCount contribute to O(N * firstTargetIndex) and O(N * N).
// Specifically, the inner loops iterate over a range of balances.
// In the worst case, firstTargetIndex can be N-1, making the first inner loop O(N).
// The second inner loop iterates up to currentBalanceFromTarget, which can be O(N).
// So, the overall complexity is O(N^2).
// This solution will TLE for N=10^5.

// Space Complexity: O(N) for leftBalanceCounts and rightBalanceCounts arrays.