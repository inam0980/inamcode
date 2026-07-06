class Solution {
    public int removeCoveredIntervals(int[][] intervals) {
        // Sort intervals: primary by start point ascending, secondary by end point descending.
        // This ensures that if two intervals have the same start, the one with the larger end
        // comes first, making it impossible for the smaller one to cover the larger one,
        // and simplifying the check for coverage.
        java.util.Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0]; // Sort by start ascending
            }
            return b[1] - a[1]; // If starts are same, sort by end descending
        });

        int coveredCount = 0;
        int maxEnd = -1; // Tracks the maximum end point encountered so far among non-covered intervals

        for (int[] interval : intervals) {
            int currentStart = interval[0];
            int currentEnd = interval[1];

            // If the current interval's end is less than or equal to maxEnd,
            // it means it's covered by a previous interval (whose start was <= currentStart
            // and end was >= currentEnd due to sorting and maxEnd tracking).
            if (currentEnd <= maxEnd) {
                coveredCount++;
            } else {
                // If not covered, update maxEnd to this interval's end.
                // This interval now potentially covers subsequent intervals.
                maxEnd = currentEnd;
            }
        }

        // The number of remaining intervals is total intervals minus covered ones.
        return intervals.length - coveredCount;
    }
}

// Time Complexity: O(N log N) due to sorting, where N is the number of intervals.
// Space Complexity: O(log N) or O(N) depending on the sort implementation (for merge sort or quicksort stack space).