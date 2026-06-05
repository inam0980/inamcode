class Solution {
    public int minimumAddedInteger(int[] nums1, int[] nums2) {
        // Sort both arrays to simplify comparison and finding potential x values
        java.util.Arrays.sort(nums1);
        java.util.Arrays.sort(nums2);

        int minX = Integer.MAX_VALUE; // Initialize minX to a very large value

        // Iterate through all possible pairs of elements to remove from nums1
        // nums1 has N elements, nums2 has N-2 elements.
        // We need to pick 2 elements from nums1 to be removed.
        // The remaining N-2 elements in nums1, after adding x, must match nums2.
        // Since nums1 and nums2 are sorted, if nums1[i] is the first element
        // that matches nums2[0] after adding x, then x = nums2[0] - nums1[i].
        // We only need to consider the first two elements of nums1 as potential candidates
        // for the first element that *remains* after removal and matches nums2[0].
        // This is because if nums1[k] (k > 1) is the first remaining element,
        // then nums1[0] and nums1[1] must have been removed.
        // The problem states that *any* two elements can be removed.
        // However, since we are looking for the *minimum* x, and nums1 and nums2 are sorted,
        // the smallest possible x will be derived from matching nums2[0] with one of
        // the smallest elements in nums1 that *could* remain.
        // The first element of nums2 (nums2[0]) must correspond to some element in nums1
        // after adding x and removing two elements.
        // This element in nums1 must be either nums1[0], nums1[1], or nums1[2].
        // If nums1[0] corresponds to nums2[0], then x = nums2[0] - nums1[0].
        // If nums1[1] corresponds to nums2[0], then x = nums2[0] - nums1[1] (nums1[0] was removed).
        // If nums1[2] corresponds to nums2[0], then x = nums2[0] - nums1[2] (nums1[0] and nums1[1] were removed).
        // Any other choice for the first matching element in nums1 (e.g., nums1[3]) would mean
        // nums1[0], nums1[1], nums1[2] were removed, which is 3 elements, not 2.
        // So, we only need to check these three potential values for x.

        for (int i = 0; i < 3; i++) { // i represents the index of nums1 that could correspond to nums2[0]
            int currentX = nums2[0] - nums1[i]; // Calculate potential x

            int nums1Ptr = 0;
            int nums2Ptr = 0;
            int removedCount = 0;

            // Try to match nums2 elements with nums1 elements + currentX
            while (nums1Ptr < nums1.length && nums2Ptr < nums2.length) {
                if (nums1[nums1Ptr] + currentX == nums2[nums2Ptr]) {
                    // Match found, advance both pointers
                    nums1Ptr++;
                    nums2Ptr++;
                } else {
                    // No match, this nums1 element must be one of the removed ones
                    nums1Ptr++;
                    removedCount++;
                }
            }

            // After the loop, check if all nums2 elements were matched
            // and exactly two elements were effectively removed from nums1.
            // The remaining elements in nums1Ptr (if any) must also be removed.
            removedCount += (nums1.length - nums1Ptr);

            if (nums2Ptr == nums2.length && removedCount == 2) {
                // If all nums2 elements were matched and exactly 2 elements were removed,
                // this currentX is a valid candidate. Update minX if it's smaller.
                minX = Math.min(minX, currentX);
            }
        }

        return minX;
    }
}
// Time Complexity: O(N log N) due to sorting. The nested loop runs a constant number of times (3 iterations), and inside each iteration, we traverse nums1 and nums2 once, which is O(N). So, O(N log N + 3*N) = O(N log N).
// Space Complexity: O(log N) or O(N) depending on the sorting algorithm used (e.g., O(log N) for quicksort stack space, O(N) for mergesort auxiliary array). If in-place sort is used, it's O(1) auxiliary space.