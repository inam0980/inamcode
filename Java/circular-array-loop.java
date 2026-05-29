class Solution {
    public boolean circularArrayLoop(int[] nums) {
        int n = nums.length;

        // Iterate through each possible starting point for a cycle
        for (int i = 0; i < n; i++) {
            // If the current index has already been visited and marked as part of a non-cycle path, skip it
            if (nums[i] == 0) {
                continue;
            }

            int slow = i;
            int fast = i;
            boolean isForward = nums[i] > 0; // Direction of the current path

            while (true) {
                // Move slow pointer one step
                slow = getNextIndex(nums, slow, isForward);
                if (slow == -1) break; // Invalid move or direction change

                // Move fast pointer two steps
                fast = getNextIndex(nums, fast, isForward);
                if (fast == -1) break;
                fast = getNextIndex(nums, fast, isForward);
                if (fast == -1) break;

                // If slow and fast pointers meet, a cycle is found
                if (slow == fast) {
                    // Check for cycle length > 1
                    if (slow == getNextIndex(nums, slow, isForward)) { // Cycle of length 1
                        break; // This is a self-loop, not a valid cycle
                    }
                    return true; // Valid cycle found
                }
            }

            // If no cycle found from 'i', mark all visited nodes in this path as 0
            // to avoid re-processing them. This is crucial for O(1) space and O(N) time.
            int current = i;
            while (nums[current] != 0) {
                int next = getNextIndex(nums, current, isForward);
                if (next == -1) break; // Path ended due to direction change or invalid move
                nums[current] = 0; // Mark as visited and part of a non-cycle path
                current = next;
            }
        }

        return false; // No cycle found after checking all starting points
    }

    // Helper function to calculate the next index in the circular array
    // Also checks if the movement direction is consistent with 'isForward'
    private int getNextIndex(int[] nums, int currentIndex, boolean isForward) {
        int n = nums.length;
        boolean currentDirection = nums[currentIndex] > 0;

        // If direction changes, this path is invalid for a cycle
        if (currentDirection != isForward) {
            return -1;
        }

        // Calculate next index using modulo for circularity
        int nextIndex = (currentIndex + nums[currentIndex]) % n;
        if (nextIndex < 0) { // Handle negative results from modulo for negative jumps
            nextIndex += n;
        }

        // If moving to the same index, it's a self-loop (cycle length 1), which is invalid
        if (nextIndex == currentIndex) {
            return -1;
        }

        return nextIndex;
    }
}

// Time Complexity: O(N)
// Space Complexity: O(1)