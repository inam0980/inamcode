class Solution {
    public int countKeyChanges(String s) {
        if (s.length() <= 1) {
            return 0; // No changes possible with 0 or 1 character
        }

        int changes = 0;
        // Convert the first character to lowercase for initial comparison
        char lastChar = Character.toLowerCase(s.charAt(0)); 

        // Iterate from the second character
        for (int i = 1; i < s.length(); i++) {
            // Convert current character to lowercase for case-insensitive comparison
            char currentChar = Character.toLowerCase(s.charAt(i));
            
            // If the current character is different from the last, increment changes
            if (currentChar != lastChar) {
                changes++;
            }
            // Update lastChar for the next iteration
            lastChar = currentChar;
        }

        return changes;
    }
}
// Time complexity: O(N) where N is the length of the string s. We iterate through the string once.
// Space complexity: O(1) as we only use a few constant extra variables.