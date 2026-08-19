class Solution {
    public long maximumSumOfHeights(List<Integer> maxHeights) {
        int n = maxHeights.size();
        long maxSum = 0;

        // Calculate prefix sums for each possible peak
        long[] prefixSum = new long[n];
        Deque<Integer> stack = new ArrayDeque<>();
        long currentSum = 0;

        for (int i = 0; i < n; i++) {
            int h = maxHeights.get(i);
            int count = 1; // Number of elements with current height
            while (!stack.isEmpty() && maxHeights.get(stack.peek()) >= h) {
                int prevIdx = stack.pop();
                int prevHeight = maxHeights.get(prevIdx);
                // Subtract contribution of elements taller than or equal to current h
                // These elements will now be capped by h
                currentSum -= (long) prevHeight * (stack.isEmpty() ? prevIdx + 1 : prevIdx - stack.peek());
            }
            // Add contribution of current height h
            // It extends from the previous smaller element's index + 1, or from 0
            currentSum += (long) h * (stack.isEmpty() ? i + 1 : i - stack.peek());
            stack.push(i);
            prefixSum[i] = currentSum;
        }

        // Calculate suffix sums for each possible peak
        long[] suffixSum = new long[n];
        stack.clear(); // Clear stack for suffix calculation
        currentSum = 0;

        for (int i = n - 1; i >= 0; i--) {
            int h = maxHeights.get(i);
            int count = 1;
            while (!stack.isEmpty() && maxHeights.get(stack.peek()) >= h) {
                int prevIdx = stack.pop();
                int prevHeight = maxHeights.get(prevIdx);
                // Subtract contribution of elements taller than or equal to current h
                currentSum -= (long) prevHeight * (stack.isEmpty() ? n - prevIdx : stack.peek() - prevIdx);
            }
            // Add contribution of current height h
            currentSum += (long) h * (stack.isEmpty() ? n - i : stack.peek() - i);
            stack.push(i);
            suffixSum[i] = currentSum;
        }

        // Combine prefix and suffix sums to find the maximum total sum
        for (int i = 0; i < n; i++) {
            // Subtract maxHeights.get(i) once because it's counted in both prefixSum[i] and suffixSum[i]
            maxSum = Math.max(maxSum, prefixSum[i] + suffixSum[i] - maxHeights.get(i));
        }

        return maxSum;
    }
}
// Time complexity: O(N) due to two passes with a monotonic stack. Each element is pushed and popped at most once.
// Space complexity: O(N) for storing prefixSum, suffixSum arrays, and the stack.