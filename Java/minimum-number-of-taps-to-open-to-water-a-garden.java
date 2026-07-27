class Solution {
    public int minTaps(int n, int[] ranges) {
        // maxReach[i] stores the maximum point reachable from point i
        // by any tap that covers point i or starts before i and extends past i.
        int[] maxReach = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            maxReach[i] = i; // Initialize with current point, meaning no tap extends it
        }

        // Calculate the maximum reach for each starting point
        for (int i = 0; i <= n; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(n, i + ranges[i]);
            // A tap at 'i' covers [left, right].
            // If we are at 'left', the best we can do is reach 'right'.
            // Update maxReach[left] to be the maximum of its current value and 'right'.
            maxReach[left] = Math.max(maxReach[left], right);
        }

        int taps = 0;
        int currentReach = 0; // The current maximum point watered
        int nextReach = 0;    // The maximum point we can reach by opening one more tap
        int i = 0;            // Pointer for iterating through the garden points

        while (currentReach < n) {
            taps++; // Open a new tap
            // Find the best tap to open from the unwatered segment
            // The segment is from currentReach to nextReach.
            // We want to find a tap that starts at or before currentReach
            // and extends as far as possible.
            // Iterate from the last watered point (i) up to currentReach.
            // For each point 'j' in [i, currentReach], maxReach[j] tells us
            // how far a tap starting at 'j' can reach.
            // We want to find the maximum of these maxReach[j] values.
            while (i <= currentReach) {
                nextReach = Math.max(nextReach, maxReach[i]);
                i++;
            }

            // If nextReach is not greater than currentReach, it means we cannot extend
            // the watered area further, so the garden cannot be fully watered.
            if (nextReach <= currentReach) {
                return -1;
            }
            currentReach = nextReach; // Update the current watered area
        }

        return taps;
    }
}
// Time Complexity: O(N)
// Space Complexity: O(N)