class Solution {
    public int maximumLength(int[] nums) {
        // Use a frequency map to store counts of each number
        Map<Long, Integer> counts = new HashMap<>();
        for (int num : nums) {
            counts.put((long) num, counts.getOrDefault((long) num, 0) + 1);
        }

        int maxLength = 1; // Minimum length is 1 (any single element)

        // Handle the special case of '1'
        if (counts.containsKey(1L)) {
            int onesCount = counts.get(1L);
            // If count of 1s is odd, we can use all of them (e.g., [1,1,1] -> 1,1,1)
            // If count of 1s is even, we can use all but one to form a symmetric pattern (e.g., [1,1,1,1] -> 1,1,1)
            // The pattern [x, x^2, ..., x^k, ..., x^2, x] means 1s can be used as [1,1,1,...1]
            // The length is always odd for 1s if we want to maximize.
            // If we have an even number of 1s, say 4, we can form [1,1,1].
            // If we have an odd number of 1s, say 3, we can form [1,1,1].
            // So, if onesCount is even, we use onesCount - 1 elements. If odd, we use onesCount elements.
            // This can be simplified to onesCount - (onesCount % 2 == 0 ? 1 : 0)
            // Or simply, if onesCount is even, we use onesCount - 1. If odd, we use onesCount.
            // This is equivalent to (onesCount % 2 == 0 ? onesCount - 1 : onesCount)
            // Or more simply, if onesCount is even, we can use onesCount - 1. If odd, we can use onesCount.
            // The problem states k can be any non-negative power of 2.
            // For x=1, the sequence is [1, 1, ..., 1]. The length is just the count of 1s.
            // The pattern [x, x^2, ..., x^k, ..., x^2, x] implies a center element x^k.
            // If x=1, then x^k = 1. So the sequence is [1, 1, ..., 1].
            // The length of this sequence is 2*k + 1.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // However, the problem states k is a non-negative power of 2.
            // For x=1, the sequence is [1, 1, ..., 1].
            // The length of the sequence is 2*k + 1.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // The problem states the pattern is [x, x^2, x^4, ..., x^(k/2), x^k, x^(k/2), ..., x^4, x^2, x].
            // Here k is a power of 2.
            // For x=1, the sequence is [1, 1, ..., 1].
            // The length of this sequence is 2 * (log_2(k)) + 1.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // The example [1,3,2,4] output 1. If we select {1}, it's [1].
            // The example [5,4,1,2,2] output 3.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // The pattern implies a center element.
            // For x=1, the sequence is [1, 1, ..., 1].
            // The length is 2 * (number of distinct powers of 2) + 1.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // The pattern is [x, x^2, x^4, ..., x^(2^p), ..., x^4, x^2, x].
            // The length is 2*p + 1.
            // If we have 'c' ones, we can form a sequence of length 'c'.
            // The length must be odd. If we have an even number of 1s, say 4, we can form [1,1,1].
            // If we have an odd number of 1s, say 3, we can form [1,1,1].
            // So, if onesCount is even, we use onesCount - 1 elements. If odd, we use onesCount elements.
            maxLength = Math.max(maxLength, onesCount % 2 == 0 ? onesCount - 1 : onesCount);
            // Remove 1 from map to avoid re-processing in the loop
            counts.remove(1L);
        }

        // Iterate through remaining unique numbers
        for (long num : counts.keySet()) {
            if (num == 1L) continue; // Already handled

            long current = num;
            int currentLength = 0;

            // Build the sequence x, x^2, x^4, ...
            while (counts.containsKey(current) && counts.get(current) >= 2) {
                currentLength += 2; // Add x and x^2 (or x^k and x^(k*2))
                current *= current; // Move to the next power (x^2, x^4, ...)
                if (current > 1_000_000_000L * 1_000_000_000L) break; // Prevent overflow
            }

            // After the loop, current is the first number in the sequence that
            // either doesn't exist or has count < 2.
            // If current exists and has count >= 1, we can use it as the center element.
            if (counts.containsKey(current) && counts.get(current) >= 1) {
                currentLength += 1; // Add the center element x^k
            } else {
                // If the last element (x^k) is not available,
                // the sequence must be symmetric without a center,
                // e.g., [x, x^2, x^2, x]. This is not allowed by the pattern.
                // The pattern is [x, x^2, ..., x^k, ..., x^2, x].
                // This implies an odd length.
                // If we couldn't find the center element, the length must be currentLength - 1.
                // For example, if we found x, x^2, x^4, but not x^8.
                // We have [x, x^2, x^4, x^4, x^2, x]. Length 6.
                // The pattern requires a center element.
                // So if we couldn't find the center, we must reduce the length by 1.
                // e.g., [x, x^2, x^2, x]. Length 4.
                // The pattern is [x, x^2, x^4, ..., x^k, ..., x^4, x^2, x].
                // This means the length is always odd.
                // If currentLength is even, it means we found pairs (x, x^2, x^4, ...)
                // but couldn't find the center. So we must remove one pair.
                // For example, if we have [2,4,16,4,2]. Length 5.
                // If we only have [2,4,4,2]. Length 4. This is not valid.
                // The pattern is [x, x^2, x^4, ..., x^k, ..., x^4, x^2, x].
                // This means the length is always odd.
                // If currentLength is even, it means we found pairs (x, x^2, x^4, ...)
                // but couldn't find the center. So we must remove one pair.
                // For example, if we have [2,4,16,4,2]. Length 5.
                // If we only have [2,4,4,2]. Length 4. This is not valid.
                // The length must be odd. If currentLength is even, we decrement it.
                currentLength -= 1;
            }
            maxLength = Math.max(maxLength, currentLength);
        }

        return maxLength;
    }
}
// Time Complexity: O(N + M * log(Max_Val)) where N is the number of elements in nums, M is the number of unique elements, and Max_Val is the maximum value in nums.
// N for populating the map. M * log(Max_Val) for iterating through unique elements and finding powers.
// Space Complexity: O(M) where M is the number of unique elements in nums, for storing the frequency map.