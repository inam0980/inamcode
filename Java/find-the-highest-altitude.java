class Solution {
    public int largestAltitude(int[] gain) {
        int currentAltitude = 0; // Biker starts at point 0 with altitude 0
        int maxAltitude = 0;     // Initialize max altitude to 0, as 0 is a possible highest point

        // Iterate through the gain array to calculate altitudes at each point
        for (int g : gain) {
            currentAltitude += g; // Add the net gain to the current altitude
            // Update maxAltitude if the current altitude is higher
            if (currentAltitude > maxAltitude) {
                maxAltitude = currentAltitude;
            }
        }

        return maxAltitude; // Return the highest altitude found
    }
}

// Time Complexity: O(n) where n is the length of the gain array. We iterate through the array once.
// Space Complexity: O(1) as we only use a few constant extra variables.