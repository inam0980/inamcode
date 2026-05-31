class Solution {
    public boolean satisfiesConditions(int[][] grid) {
        int m = grid.length;    // Number of rows
        int n = grid[0].length; // Number of columns

        // Iterate through each cell in the grid
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Condition 1: Check if cell is equal to the cell below it
                // This condition applies only if a cell below exists (i < m - 1)
                if (i < m - 1) {
                    if (grid[i][j] != grid[i + 1][j]) {
                        return false; // Condition violated
                    }
                }

                // Condition 2: Check if cell is different from the cell to its right
                // This condition applies only if a cell to the right exists (j < n - 1)
                if (j < n - 1) {
                    if (grid[i][j] == grid[i][j + 1]) {
                        return false; // Condition violated
                    }
                }
            }
        }

        // If all cells satisfy both conditions, return true
        return true;
    }
}
// Time complexity: O(m * n) where m is the number of rows and n is the number of columns.
// Space complexity: O(1) as no extra space proportional to input size is used.