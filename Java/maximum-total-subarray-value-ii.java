class Solution {
    public long maxTotalValue(int[] nums, int k) {
        int n = nums.length;
        // Store all possible subarray values
        // Use a min-priority queue to keep track of the k largest values
        // If the size exceeds k, remove the smallest element
        java.util.PriorityQueue<Long> minHeap = new java.util.PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            int currentMin = nums[i];
            int currentMax = nums[i];
            for (int j = i; j < n; j++) {
                currentMin = Math.min(currentMin, nums[j]);
                currentMax = Math.max(currentMax, nums[j]);
                long value = (long) currentMax - currentMin;

                minHeap.offer(value);
                if (minHeap.size() > k) {
                    minHeap.poll(); // Remove the smallest element if heap size exceeds k
                }
            }
        }

        // Sum the k largest values remaining in the heap
        long totalValue = 0;
        while (!minHeap.isEmpty()) {
            totalValue += minHeap.poll();
        }

        return totalValue;
    }
}
// Time complexity: O(N^2 * log K)
// Space complexity: O(K)