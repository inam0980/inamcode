class Solution {
    public int earliestFinishTime(int[] landStartTime, int[] landDuration, int[] waterStartTime, int[] waterDuration) {
        int minOverallFinishTime = Integer.MAX_VALUE;

        // Iterate through all possible land rides
        for (int i = 0; i < landStartTime.length; i++) {
            int landStart = landStartTime[i];
            int landDur = landDuration[i];

            // Calculate finish time if land ride is taken first
            int landFinish = landStart + landDur;

            // Find the best water ride to take after this land ride
            int minWaterFinishAfterLand = Integer.MAX_VALUE;
            for (int j = 0; j < waterStartTime.length; j++) {
                int waterStart = waterStartTime[j];
                int waterDur = waterDuration[j];

                // Water ride can start at max(landFinish, waterStart)
                int actualWaterStart = Math.max(landFinish, waterStart);
                minWaterFinishAfterLand = Math.min(minWaterFinishAfterLand, actualWaterStart + waterDur);
            }
            minOverallFinishTime = Math.min(minOverallFinishTime, minWaterFinishAfterLand);
        }

        // Iterate through all possible water rides
        for (int j = 0; j < waterStartTime.length; j++) {
            int waterStart = waterStartTime[j];
            int waterDur = waterDuration[j];

            // Calculate finish time if water ride is taken first
            int waterFinish = waterStart + waterDur;

            // Find the best land ride to take after this water ride
            int minLandFinishAfterWater = Integer.MAX_VALUE;
            for (int i = 0; i < landStartTime.length; i++) {
                int landStart = landStartTime[i];
                int landDur = landDuration[i];

                // Land ride can start at max(waterFinish, landStart)
                int actualLandStart = Math.max(waterFinish, landStart);
                minLandFinishAfterWater = Math.min(minLandFinishAfterWater, actualLandStart + landDur);
            }
            minOverallFinishTime = Math.min(minOverallFinishTime, minLandFinishAfterWater);
        }

        return minOverallFinishTime;
    }
}
// Time complexity: O(N*M) where N is the number of land rides and M is the number of water rides.
// Space complexity: O(1)