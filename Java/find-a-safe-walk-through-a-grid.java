class Solution {
    public boolean findSafeWalk(List<List<Integer>> grid, int health) {
        int m = grid.size();
        int n = grid.get(0).size();

        // dp[r][c] stores the maximum health remaining when reaching cell (r, c)
        // Initialize with a very small number to represent unreachable states
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }

        // PriorityQueue for Dijkstra-like approach: {max_health, row, col}
        // We want to process cells with higher health first
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        // Starting cell (0,0)
        // Initial health is reduced if (0,0) is unsafe
        int initialHealth = health - grid.get(0).get(0);
        if (initialHealth <= 0) { // If health drops to 0 or less at start, it's not a safe walk
            return false;
        }
        dp[0][0] = initialHealth;
        pq.offer(new int[]{initialHealth, 0, 0});

        int[] dr = {-1, 1, 0, 0}; // Row changes for Up, Down, Left, Right
        int[] dc = {0, 0, -1, 1}; // Column changes for Up, Down, Left, Right

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentHealth = current[0];
            int r = current[1];
            int c = current[2];

            // If we've found a better path to (r,c) already, skip this one
            if (currentHealth < dp[r][c]) {
                continue;
            }

            // If we reached the destination with positive health, return true
            if (r == m - 1 && c == n - 1) {
                return true;
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                // Check bounds
                if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                    // Calculate new health for the neighbor
                    int newHealth = currentHealth - grid.get(nr).get(nc);

                    // If new health is positive and better than previously recorded for (nr, nc)
                    if (newHealth > 0 && newHealth > dp[nr][nc]) {
                        dp[nr][nc] = newHealth;
                        pq.offer(new int[]{newHealth, nr, nc});
                    }
                }
            }
        }

        // If the loop finishes and we haven't returned true, destination is unreachable with positive health
        return false;
    }
}
// Time Complexity: O(M*N log(M*N)) due to Dijkstra with PriorityQueue. Each cell can be added to PQ at most M*N times.
// Space Complexity: O(M*N) for the DP table and the PriorityQueue in the worst case.