import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays; // Not strictly needed for this solution but good practice for array ops

class Solution {
    public List<Integer> findMissingElements(int[] nums) {
        // Use a HashSet for efficient O(1) average time complexity lookups
        Set<Integer> presentNumbers = new HashSet<>();
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;

        // Populate the set and find min/max values in a single pass
        for (int num : nums) {
            presentNumbers.add(num);
            if (num < minVal) {
                minVal = num;
            }
            if (num > maxVal) {
                maxVal = num;
            }
        }

        List<Integer> missingElements = new ArrayList<>();

        // Iterate from minVal to maxVal (inclusive) and check for missing numbers
        for (int i = minVal + 1; i < maxVal; i++) { // Start from minVal + 1 and end before maxVal
            if (!presentNumbers.contains(i)) {
                missingElements.add(i);
            }
        }

        return missingElements;
    }
}

// Time complexity: O(N) where N is the number of elements in nums.
// This is due to one pass to populate the HashSet and find min/max, and another pass from minVal to maxVal (at most 100 iterations based on constraints).
// Space complexity: O(N) in the worst case for the HashSet (if all numbers are unique and within a small range)
// and O(M) for the result list, where M is the number of missing elements.
// Given constraints (nums[i] <= 100), the max range size is 100, so space is effectively O(100) which is constant.