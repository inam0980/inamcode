class Solution {
    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        int m = people.size();

        // Map skills to unique integer IDs for bitmasking
        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        // Convert each person's skills into a bitmask
        int[] peopleSkillMasks = new int[m];
        for (int i = 0; i < m; i++) {
            int currentPersonSkillMask = 0;
            for (String skill : people.get(i)) {
                currentPersonSkillMask |= (1 << skillToIndex.get(skill));
            }
            peopleSkillMasks[i] = currentPersonSkillMask;
        }

        // dp[skill_mask] stores the smallest team (represented by a bitmask of people indices)
        // that can cover the skills in skill_mask.
        // Initialize with a large value (all people) for all skill masks except 0.
        // The value is a long to store a bitmask of people indices.
        long[] dp = new long[1 << n];
        Arrays.fill(dp, (1L << m) - 1); // Initialize with all people (max possible team)
        dp[0] = 0L; // No skills required, no people needed

        // Iterate through all possible skill masks
        for (int skillMask = 0; skillMask < (1 << n); skillMask++) {
            // If the current skillMask is already covered by a team, consider adding more people
            // This ensures we process skillMasks in increasing order of covered skills
            // and allows us to build up solutions from smaller skill sets.

            // Iterate through each person
            for (int pIdx = 0; pIdx < m; pIdx++) {
                // Calculate the new skill mask if this person is added
                int newSkillMask = skillMask | peopleSkillMasks[pIdx];

                // Get the current team for the current skillMask
                long currentTeam = dp[skillMask];
                // Create a new team by adding person pIdx
                long newTeam = currentTeam | (1L << pIdx);

                // If the new team is smaller (fewer people) or same size but lexicographically smaller
                // (though problem doesn't require lexicographical, just size)
                // we update the dp table. Here, we only care about size.
                // The number of set bits in the long represents the team size.
                if (Long.bitCount(newTeam) < Long.bitCount(dp[newSkillMask])) {
                    dp[newSkillMask] = newTeam;
                }
            }
        }

        // The final answer is the team for all required skills (mask (1 << n) - 1)
        long finalTeamMask = dp[(1 << n) - 1];

        // Convert the team bitmask to an array of person indices
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (((finalTeamMask >> i) & 1) == 1) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }
}

// Time Complexity: O(2^N * M) where N is the number of required skills and M is the number of people.
// The outer loop iterates 2^N times (for each possible skill mask).
// The inner loop iterates M times (for each person).
// Inside the loop, bitwise operations and bitCount are O(1) (or O(M) for bitCount on long, but M <= 60 is small constant).
// Space Complexity: O(2^N) for the dp array.
// The skillToIndex map and peopleSkillMasks array take O(N) and O(M) respectively.