class Solution {
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        // Map to store unique active minutes for each user ID
        // Key: User ID, Value: Set of unique minutes
        java.util.Map<Integer, java.util.Set<Integer>> userActivity = new java.util.HashMap<>();

        // Iterate through logs to populate userActivity map
        for (int[] log : logs) {
            int userId = log[0];
            int time = log[1];
            // Add the minute to the set for the corresponding user ID
            userActivity.computeIfAbsent(userId, k_ -> new java.util.HashSet<>()).add(time);
        }

        // Array to store the final answer, 1-indexed
        // answer[j] will store the count of users with UAM = j+1
        int[] answer = new int[k];

        // Iterate through the userActivity map to calculate UAM for each user
        for (java.util.Set<Integer> minutes : userActivity.values()) {
            int uam = minutes.size(); // UAM is the size of the set of unique minutes
            // If UAM is within the valid range [1, k], increment the corresponding count
            if (uam >= 1 && uam <= k) {
                answer[uam - 1]++; // Adjust for 0-indexed array
            }
        }

        return answer;
    }
}

// Time Complexity: O(N) where N is the number of logs.
// We iterate through the logs once to populate the map.
// Then we iterate through the unique users (at most N users) to calculate UAMs.
// Set insertion and size calculation are O(1) on average.
// Space Complexity: O(N) in the worst case.
// The map stores at most N unique user IDs. Each set can store at most M unique minutes (where M is max timei, 10^5).
// In the worst case, if all logs are for different users and different times, the total size of all sets could be N.
// If all logs are for one user, the set could store up to N unique minutes.
// The total number of unique minutes across all users is at most N.