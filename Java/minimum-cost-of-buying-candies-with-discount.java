class Solution {
    public int minimumCost(int[] cost) {
        // Sort the candies in descending order to easily pick the most expensive ones
        java.util.Arrays.sort(cost);

        int totalCost = 0;
        int n = cost.length;

        // Iterate from the most expensive candies
        for (int i = n - 1; i >= 0; i--) {
            // Add the cost of the current most expensive candy
            totalCost += cost[i];

            // If there's another candy (i-1), add its cost too
            // This forms a pair of two bought candies
            if (i - 1 >= 0) {
                totalCost += cost[i - 1];
                // Skip the next candy (i-2) as it will be taken for free
                // This is valid because cost[i-2] <= cost[i-1] <= cost[i]
                // So cost[i-2] is less than or equal to min(cost[i], cost[i-1])
                i -= 2; 
            } else {
                // If only one candy is left, it's bought without a free one
                break;
            }
        }

        return totalCost;
    }
}
// Time complexity: O(N log N) due to sorting, where N is the number of candies.
// Space complexity: O(1) if in-place sort is used, or O(log N) to O(N) for some sorting algorithms' auxiliary space.