class Solution {
    public int maxScore(int n, int k, int[][] stayScore, int[][] travelScore) {
        // dp[i][j] represents the maximum score ending on day i in city j.
        int[][] dp = new int[k][n];

        // Initialize day 0 scores.
        // On day 0, the tourist can start in any city and stay there.
        for (int j = 0; j < n; j++) {
            dp[0][j] = stayScore[0][j];
        }

        // Iterate through each day from day 1 to day k-1.
        for (int i = 1; i < k; i++) {
            // Find the maximum score from the previous day, considering all cities.
            // This is used for the "travel" option to optimize calculation.
            int maxPrevDayScore = 0;
            for (int prevCity = 0; prevCity < n; prevCity++) {
                maxPrevDayScore = Math.max(maxPrevDayScore, dp[i - 1][prevCity]);
            }

            // Calculate dp[i][j] for each city j on the current day i.
            for (int currCity = 0; currCity < n; currCity++) {
                // Option 1: Stay in the current city.
                // Score is previous day's score in currCity + stayScore for today.
                int stayOption = dp[i - 1][currCity] + stayScore[i][currCity];

                // Option 2: Travel to the current city from any other city.
                // This can be optimized.
                // The score would be max(dp[i-1][prevCity] + travelScore[prevCity][currCity])
                // for all prevCity != currCity.
                // This can be rewritten as:
                // maxPrevDayScore + travelScore[prevCity][currCity] - travelScore[prevCity][prevCity] (if prevCity == currCity)
                // Since travelScore[prevCity][prevCity] is 0, this simplifies.
                // The travel option from any city prevCity to currCity is:
                // dp[i-1][prevCity] + travelScore[prevCity][currCity].
                // We need to find the maximum of this over all prevCity.
                // A naive approach would be O(N) for each currCity, leading to O(N^2) per day.
                // We can optimize this by observing that travelScore[prevCity][currCity] is added.
                // The maximum score for traveling to currCity is:
                // max(dp[i-1][prevCity] - travelScore[currCity][prevCity]) + travelScore[currCity][prevCity]
                // This is not correct.
                // The correct optimization for travel is:
                // max(dp[i-1][prevCity] + travelScore[prevCity][currCity]) for all prevCity.
                // This is still O(N) per currCity.
                // Let's re-evaluate the travel option.
                // The maximum score to reach currCity on day i by traveling is:
                // max_{prevCity != currCity} (dp[i-1][prevCity] + travelScore[prevCity][currCity])
                // This is indeed O(N) for each currCity.
                // So, for each currCity, we iterate through all prevCity to find the best travel option.
                int travelOption = 0;
                for (int prevCity = 0; prevCity < n; prevCity++) {
                    if (prevCity == currCity) {
                        // If prevCity is currCity, this is effectively staying,
                        // but the problem defines travel as moving to *another* city.
                        // However, the travelScore[i][i] is 0, so if we consider it,
                        // it would be dp[i-1][currCity] + 0, which is covered by stayOption.
                        // For clarity, we only consider actual travel.
                        continue;
                    }
                    travelOption = Math.max(travelOption, dp[i - 1][prevCity] + travelScore[prevCity][currCity]);
                }
                
                dp[i][currCity] = Math.max(stayOption, travelOption);
            }
        }

        // After k days, find the maximum score among all possible ending cities.
        int maxTotalScore = 0;
        for (int j = 0; j < n; j++) {
            maxTotalScore = Math.max(maxTotalScore, dp[k - 1][j]);
        }

        return maxTotalScore;
    }
}

/*
Time Complexity: O(k * n^2)
Space Complexity: O(k * n)
*/