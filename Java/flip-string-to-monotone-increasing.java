class Solution {
    public int minFlipsMonoIncr(String s) {
        int n = s.length();
        int flips = 0; // Flips needed if current character is '1'
        int onesCount = 0; // Count of '1's encountered so far

        // Iterate through the string
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                // If current char is '0', we have two choices:
                // 1. Keep it '0': This means all previous '1's must be flipped to '0'.
                //    The cost is 'onesCount'.
                // 2. Flip it to '1': This means it becomes part of the '1's section.
                //    The cost is 'flips + 1' (current '0' flipped to '1').
                // We take the minimum of these two options.
                flips = Math.min(onesCount, flips + 1);
            } else {
                // If current char is '1', it can either be part of the '0's section (flipped to '0')
                // or part of the '1's section (kept as '1').
                // If kept as '1', it contributes to 'onesCount'.
                onesCount++;
            }
        }

        return flips;
    }
}

// Time complexity: O(N), where N is the length of the string. We iterate through the string once.
// Space complexity: O(1), as we only use a few constant extra variables.