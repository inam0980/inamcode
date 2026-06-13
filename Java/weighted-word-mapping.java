class Solution {
    public String mapWordWeights(String[] words, int[] weights) {
        StringBuilder result = new StringBuilder(); // Stores the mapped characters

        // Iterate through each word in the input array
        for (String word : words) {
            int currentWordWeight = 0; // Accumulator for the current word's total weight

            // Calculate the weight of the current word
            for (char c : word.toCharArray()) {
                // 'a' has an ASCII value of 97. Subtracting 'a' gives 0 for 'a', 1 for 'b', etc.
                currentWordWeight += weights[c - 'a'];
            }

            // Apply modulo 26 to the total word weight
            int mappedIndex = currentWordWeight % 26;

            // Map the result to a lowercase English letter using reverse alphabetical order
            // 'z' is 0, 'y' is 1, ..., 'a' is 25
            // The character for mappedIndex is 'z' - mappedIndex
            char mappedChar = (char) ('z' - mappedIndex);
            result.append(mappedChar); // Append the mapped character to the result
        }

        return result.toString(); // Return the final concatenated string
    }
}
// Time Complexity: O(N * L), where N is the number of words and L is the maximum length of a word.
// Space Complexity: O(N) for the StringBuilder, as in the worst case, it stores N characters.