class Solution {
    public int earliestFinishTime(int[] landStartTime, int[] landDuration, int[] waterStartTime, int[] waterDuration) {
        int minOverallFinishTime = Integer.MAX_VALUE;

        // Iterate through all possible land rides
        for (int i = 0; i < landStartTime.length; i++) {
            // Iterate through all possible water rides
            for (int j = 0; j < waterStartTime.length; j++) {
                // Calculate finish time if land ride is taken first
                int landFinishTime = landStartTime[i] + landDuration[i];
                // Water ride can start after land ride finishes or after its own opening time, whichever is later
                int waterStartAfterLand = Math.max(landFinishTime, waterStartTime[j]);
                int totalFinishTimeLandFirst = waterStartAfterLand + waterDuration[j];
                minOverallFinishTime = Math.min(minOverallFinishTime, totalFinishTimeLandFirst);

                // Calculate finish time if water ride is taken first
                int waterFinishTime = waterStartTime[j] + waterDuration[j];
                // Land ride can start after water ride finishes or after its own opening time, whichever is later
                int landStartAfterWater = Math.max(waterFinishTime, landStartTime[i]);
                int totalFinishTimeWaterFirst = landStartAfterWater + landDuration[i];
                minOverallFinishTime = Math.min(minOverallFinishTime, totalFinishTimeWaterFirst);
            }
        }

        return minOverallFinishTime;
    }
}
// Time Complexity: O(n*m) where n is the number of land rides and m is the number of water rides.
// Space Complexity: O(1)