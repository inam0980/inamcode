class Solution {
    public List<String> commonChars(String[] words) {
        // Initialize an array to store the minimum frequency of each character across all words
        // Size 26 for 'a' through 'z'
        // Initialize with maximum possible value (or frequency from the first word)
        int[] minFreq = new int[26];
        for (int i = 0; i < 26; i++) {
            minFreq[i] = Integer.MAX_VALUE;
        }

        // Iterate through each word in the input array
        for (String word : words) {
            // Create a frequency array for the current word
            int[] currentWordFreq = new int[26];
            for (char c : word.toCharArray()) {
                currentWordFreq[c - 'a']++;
            }

            // Update minFreq: for each character, take the minimum of its current minFreq
            // and its frequency in the current word
            for (int i = 0; i < 26; i++) {
                minFreq[i] = Math.min(minFreq[i], currentWordFreq[i]);
            }
        }

        // Build the result list based on the final minFreq array
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            // Add character 'i' (converted back from index) as many times as its minFreq
            for (int j = 0; j < minFreq[i]; j++) {
                result.add(String.valueOf((char) ('a' + i)));
            }
        }

        return result;
    }
}
// Time Complexity: O(N * L), where N is the number of words and L is the average length of a word.
// Space Complexity: O(1) because the frequency arrays are of fixed size 26, regardless of input size.