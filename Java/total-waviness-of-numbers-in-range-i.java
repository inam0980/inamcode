class Solution {
    public int totalWaviness(int num1, int num2) {
        int totalWavinessSum = 0;

        // Iterate through each number in the given range [num1, num2]
        for (int i = num1; i <= num2; i++) {
            totalWavinessSum += calculateWaviness(i);
        }

        return totalWavinessSum;
    }

    // Helper method to calculate the waviness of a single number
    private int calculateWaviness(int num) {
        String s = String.valueOf(num);
        int n = s.length();

        // Numbers with fewer than 3 digits have a waviness of 0
        if (n < 3) {
            return 0;
        }

        int waviness = 0;
        // Iterate from the second digit to the second-to-last digit
        // The first and last digits cannot be peaks or valleys
        for (int i = 1; i < n - 1; i++) {
            int prev = Character.getNumericValue(s.charAt(i - 1));
            int current = Character.getNumericValue(s.charAt(i));
            int next = Character.getNumericValue(s.charAt(i + 1));

            // Check for peak condition
            if (current > prev && current > next) {
                waviness++;
            }
            // Check for valley condition
            else if (current < prev && current < next) {
                waviness++;
            }
        }
        return waviness;
    }
}

// Time complexity: O((num2 - num1 + 1) * log10(num2))
// Space complexity: O(log10(num2))