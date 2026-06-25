class Solution {
    public int countMajoritySubarrays(int[] nums, int target) {
        int n = nums.length;
        int count = 0;

        // Iterate through all possible start indices
        for (int i = 0; i < n; i++) {
            int targetCount = 0;
            int otherCount = 0;
            // Iterate through all possible end indices for the current start index
            for (int j = i; j < n; j++) {
                if (nums[j] == target) {
                    targetCount++;
                } else {
                    otherCount++;
                }
                // Check if target is the majority element in the current subarray
                if (targetCount > otherCount) {
                    count++;
                }
            }
        }
        return count;
    }
}

// Time Complexity: O(N^2)
// Space Complexity: O(1)