class Solution {
    public String makeFancyString(String s) {
        if (s == null || s.length() < 3) {
            return s;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(s.charAt(0)); // Always add the first character
        sb.append(s.charAt(1)); // Always add the second character

        // Iterate from the third character
        for (int i = 2; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            // Check if the current character forms three consecutive identical characters
            // with the last two characters in the StringBuilder
            if (currentChar != sb.charAt(sb.length() - 1) || currentChar != sb.charAt(sb.length() - 2)) {
                sb.append(currentChar); // If not, append it
            }
            // If it does form three consecutive, skip it (effectively deleting it)
        }

        return sb.toString();
    }
}
// Time Complexity: O(N) where N is the length of the input string s. We iterate through the string once.
// Space Complexity: O(N) in the worst case, if no characters are deleted (e.g., "abcde"), the StringBuilder will store N characters.