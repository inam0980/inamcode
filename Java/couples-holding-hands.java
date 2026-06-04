class Solution {
    public int minSwapsCouples(int[] row) {
        int n = row.length;
        // Map to store the current position of each person
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pos[row[i]] = i;
        }

        int swaps = 0;
        // Iterate through the seats in pairs
        for (int i = 0; i < n; i += 2) {
            int p1 = row[i]; // Person in the current seat
            int p2 = row[i + 1]; // Person in the next seat

            // Determine the ideal partner for p1
            int idealPartner = (p1 % 2 == 0) ? p1 + 1 : p1 - 1;

            // If p2 is not the ideal partner, a swap is needed
            if (p2 != idealPartner) {
                swaps++;
                // Find the current position of the ideal partner
                int idealPartnerPos = pos[idealPartner];

                // Swap the person at (i+1) with the ideal partner
                // Update row array
                row[idealPartnerPos] = p2;
                row[i + 1] = idealPartner;

                // Update pos array for the swapped people
                pos[p2] = idealPartnerPos;
                pos[idealPartner] = i + 1;
            }
        }
        return swaps;
    }
}
// Time Complexity: O(N) where N is the length of the row array.
// Space Complexity: O(N) for the position array.