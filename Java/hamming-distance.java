class Solution {
    public int hammingDistance(int x, int y) {
        // Calculate the XOR of x and y. This results in a number where
        // each set bit (1) indicates a position where x and y had different bits.
        int xorResult = x ^ y;
        
        // Initialize a counter for the Hamming distance.
        int distance = 0;
        
        // Iterate while xorResult is not zero.
        // In each iteration, we count one set bit and remove it.
        while (xorResult != 0) {
            // Increment distance for each set bit found.
            distance++;
            // Brian Kernighan's algorithm: xorResult & (xorResult - 1)
            // unsets the least significant set bit.
            xorResult = xorResult & (xorResult - 1);
        }
        
        // Return the total count of different bits.
        return distance;
    }
}
// Time Complexity: O(log(max(x, y))) or O(number of bits in integer)
// Space Complexity: O(1)