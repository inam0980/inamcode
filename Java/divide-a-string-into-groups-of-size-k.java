class Solution {
    public String[] divideString(String s, int k, char fill) {
        int n = s.length();
        // Calculate the number of groups needed.
        // (n + k - 1) / k is equivalent to Math.ceil((double)n / k) for integers.
        int numGroups = (n + k - 1) / k; 
        String[] result = new String[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int startIndex = i * k;
            int endIndex = Math.min(startIndex + k, n); // Ensure endIndex doesn't exceed string length

            // Extract the substring for the current group
            StringBuilder currentGroup = new StringBuilder(s.substring(startIndex, endIndex));

            // If the current group is the last group and needs padding
            if (currentGroup.length() < k) {
                // Append fill characters until the group reaches size k
                while (currentGroup.length() < k) {
                    currentGroup.append(fill);
                }
            }
            result[i] = currentGroup.toString();
        }

        return result;
    }
}
// Time complexity: O(N), where N is the length of the string s. We iterate through the string once to form groups.
// Space complexity: O(N), where N is the length of the string s. The result array stores substrings, total length N.