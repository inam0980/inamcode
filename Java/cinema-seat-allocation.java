class Solution {
    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        // Use a HashMap to store reserved seats for each row.
        // Key: row number, Value: a bitmask representing reserved seats in that row.
        // Bitmask: 10 bits, where bit i (0-indexed) is 1 if seat i+1 is reserved.
        // For example, if seat 2 is reserved, bit 1 is set.
        // Seats are 1-indexed in problem, so seat 's' corresponds to bit 's-1'.
        java.util.Map<Integer, Integer> rowReservations = new java.util.HashMap<>();

        for (int[] seat : reservedSeats) {
            int row = seat[0];
            int col = seat[1];
            // Set the bit corresponding to the reserved seat.
            // (1 << (col - 1)) creates a bitmask with only the col-1 bit set.
            // ORing it with existing mask updates the row's reservation status.
            rowReservations.put(row, rowReservations.getOrDefault(row, 0) | (1 << (col - 1)));
        }

        int totalFamilies = 0;

        // Iterate through rows with reservations.
        for (int row : rowReservations.keySet()) {
            int mask = rowReservations.get(row);
            int familiesInRow = 0;

            // Check for the middle block (seats 4,5,6,7).
            // This corresponds to bits 3,4,5,6.
            // (mask & 0b011110000) checks if any of these bits are set in the mask.
            // If the result is 0, it means all seats 4,5,6,7 are free.
            boolean middleBlockAvailable = (mask & 0b0000111100) == 0; // Seats 4,5,6,7 (bits 3,4,5,6)

            // Check for left block (seats 2,3,4,5).
            // This corresponds to bits 1,2,3,4.
            boolean leftBlockAvailable = (mask & 0b0000011110) == 0; // Seats 2,3,4,5 (bits 1,2,3,4)

            // Check for right block (seats 6,7,8,9).
            // This corresponds to bits 5,6,7,8.
            boolean rightBlockAvailable = (mask & 0b0001111000) == 0; // Seats 6,7,8,9 (bits 5,6,7,8)

            if (leftBlockAvailable && rightBlockAvailable) {
                // If both left and right blocks are available, we can seat two groups.
                // This is the optimal scenario for a row.
                familiesInRow = 2;
            } else if (leftBlockAvailable || rightBlockAvailable || middleBlockAvailable) {
                // If only one of the three blocks is available, we can seat one group.
                // Note: The middle block overlaps with both left and right.
                // If left and right are not both available, but one of them or the middle is,
                // we can still seat one group.
                familiesInRow = 1;
            }
            totalFamilies += familiesInRow;
        }

        // Add families from rows that have no reservations.
        // These rows can always accommodate two groups (left and right blocks).
        // The number of such rows is n - rowReservations.size().
        totalFamilies += (n - rowReservations.size()) * 2;

        return totalFamilies;
    }
}

// Time Complexity: O(R + N_unique), where R is the number of reserved seats and N_unique is the number of unique rows with reservations.
// R for processing reservedSeats array and N_unique for iterating through the map.
// In the worst case, N_unique can be min(N, R). So, it's effectively O(R).
// Space Complexity: O(N_unique) for storing row reservations in the HashMap.
// N_unique is at most min(N, R).