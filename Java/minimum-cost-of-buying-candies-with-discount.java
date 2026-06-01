class Solution {
    public int minimumCost(int[] cost) {
        // Sort the candies in descending order to easily pick the most expensive ones
        java.util.Arrays.sort(cost);

        int totalCost = 0;
        int n = cost.length;

        // Iterate from the most expensive candies
        for (int i = n - 1; i >= 0; ) {
            // Buy the most expensive candy
            totalCost += cost[i];
            i--;

            // If there's another candy, buy it too
            if (i >= 0) {
                totalCost += cost[i];
                i--;
            }

            // If there's a third candy, it's the cheapest of the three considered,
            // so we can take it for free. Just skip it.
            if (i >= 0) {
                i--; // Skip this candy
            }
        }

        return totalCost;
    }
}
// Time complexity: O(N log N) due to sorting, where N is the number of candies.
// Space complexity: O(1) if in-place sort is used, or O(log N) to O(N) for some sorting algorithms.