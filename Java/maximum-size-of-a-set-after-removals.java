import java.util.HashSet;
import java.util.Set;

class Solution {
    public int maximumSetSize(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int halfN = n / 2;

        // Use HashSets to store unique elements from each array
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        Set<Integer> common = new HashSet<>();

        // Populate set1 and identify common elements
        for (int num : nums1) {
            set1.add(num);
        }

        // Populate set2 and identify common elements
        for (int num : nums2) {
            set2.add(num);
            if (set1.contains(num)) {
                common.add(num);
            }
        }

        // Calculate counts of unique elements in each set and common elements
        int unique1 = set1.size();
        int unique2 = set2.size();
        int commonCount = common.size();

        // Elements unique to nums1 (not in nums2)
        int onlyIn1 = unique1 - commonCount;
        // Elements unique to nums2 (not in nums1)
        int onlyIn2 = unique2 - commonCount;

        // Max elements we can keep from nums1 is halfN. We prioritize keeping unique elements.
        // We can keep at most 'halfN' elements from nums1.
        // We can keep at most 'onlyIn1' unique elements from nums1.
        int keptFrom1 = Math.min(onlyIn1, halfN);

        // Max elements we can keep from nums2 is halfN. We prioritize keeping unique elements.
        // We can keep at most 'halfN' elements from nums2.
        // We can keep at most 'onlyIn2' unique elements from nums2.
        int keptFrom2 = Math.min(onlyIn2, halfN);

        // Remaining slots for elements from nums1 after keeping 'onlyIn1' elements
        int remainingSlots1 = halfN - keptFrom1;
        // Remaining slots for elements from nums2 after keeping 'onlyIn2' elements
        int remainingSlots2 = halfN - keptFrom2;

        // The number of common elements we can keep is limited by:
        // 1. The actual number of common elements available (commonCount)
        // 2. The remaining slots in nums1 (remainingSlots1)
        // 3. The remaining slots in nums2 (remainingSlots2)
        // We can pick common elements to fill remaining slots in *either* array.
        // So, we can pick at most (remainingSlots1 + remainingSlots2) common elements.
        int keptCommon = Math.min(commonCount, remainingSlots1 + remainingSlots2);

        // The total maximum size of the set is the sum of:
        // 1. Unique elements kept from nums1 (onlyIn1, up to halfN)
        // 2. Unique elements kept from nums2 (onlyIn2, up to halfN)
        // 3. Common elements kept (up to commonCount, filling remaining slots)
        return keptFrom1 + keptFrom2 + keptCommon;
    }
}
/*
Time Complexity: O(N)
Space Complexity: O(N)
*/