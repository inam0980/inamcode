class Solution {
    public int maxBuilding(int n, int[][] restrictions) {
        // Add building 1 with height 0 as a restriction
        // Add building n with max height n-1 (or effectively infinity) as a restriction
        // This simplifies boundary conditions and ensures all buildings are covered
        List<int[]> processedRestrictions = new ArrayList<>();
        processedRestrictions.add(new int[]{1, 0});
        for (int[] r : restrictions) {
            processedRestrictions.add(r);
        }

        // Sort restrictions by building ID
        Collections.sort(processedRestrictions, (a, b) -> a[0] - b[0]);

        // If the last building isn't restricted, add a "virtual" restriction
        // This helps calculate heights up to building n
        if (processedRestrictions.get(processedRestrictions.size() - 1)[0] != n) {
            processedRestrictions.add(new int[]{n, n - 1}); // Max possible height for building n is n-1
        }

        // Forward pass: Adjust max heights based on left neighbors
        // For each building i, its height cannot exceed height[i-1] + 1
        for (int i = 1; i < processedRestrictions.size(); i++) {
            int[] prev = processedRestrictions.get(i - 1);
            int[] curr = processedRestrictions.get(i);
            // Current building's max height is limited by previous building's max height + distance
            curr[1] = Math.min(curr[1], prev[1] + (curr[0] - prev[0]));
        }

        // Backward pass: Adjust max heights based on right neighbors
        // For each building i, its height cannot exceed height[i+1] + 1
        for (int i = processedRestrictions.size() - 2; i >= 0; i--) {
            int[] prev = processedRestrictions.get(i + 1); // prev here refers to the one on the right
            int[] curr = processedRestrictions.get(i);
            // Current building's max height is limited by next building's max height + distance
            curr[1] = Math.min(curr[1], prev[1] + (prev[0] - curr[0]));
        }

        int maxOverallHeight = 0;

        // Calculate max height between adjacent restricted buildings
        for (int i = 0; i < processedRestrictions.size() - 1; i++) {
            int[] p1 = processedRestrictions.get(i);
            int[] p2 = processedRestrictions.get(i + 1);

            // The maximum height between p1 and p2 occurs at the "peak"
            // The peak height is determined by the lower of p1's height + distance to peak
            // and p2's height + distance to peak.
            // The formula for the peak height between two points (x1, y1) and (x2, y2)
            // with slope constraints is (y1 + y2 + |x1 - x2|) / 2
            int peakHeight = (p1[1] + p2[1] + (p2[0] - p1[0])) / 2;
            maxOverallHeight = Math.max(maxOverallHeight, peakHeight);
        }

        return maxOverallHeight;
    }
}
// Time Complexity: O(R log R) where R is the number of restrictions. Sorting takes O(R log R), passes take O(R).
// Space Complexity: O(R) for storing processed restrictions.