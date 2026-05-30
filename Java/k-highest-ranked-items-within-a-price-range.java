import java.util.*;

class Solution {
    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int low = pricing[0];
        int high = pricing[1];

        // Queue for BFS: stores {row, col, distance}
        Queue<int[]> queue = new LinkedList<>();
        // Visited array to prevent cycles and redundant processing
        boolean[][] visited = new boolean[m][n];

        // List to store all valid items found, along with their rank criteria
        // Each item is {distance, price, row, col}
        List<int[]> items = new ArrayList<>();

        // Directions for BFS (up, down, left, right)
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        // Start BFS from the given start position
        queue.offer(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int dist = curr[2];
            int price = grid[r][c];

            // If current cell contains an item within the price range, add it to items list
            if (price >= low && price <= high) {
                items.add(new int[]{dist, price, r, c});
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                // Check bounds, wall (0), and visited status
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] != 0 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc, dist + 1});
                }
            }
        }

        // Sort the items based on the ranking criteria
        // 1. Distance (shorter first)
        // 2. Price (lower first)
        // 3. Row (smaller first)
        // 4. Column (smaller first)
        Collections.sort(items, (a, b) -> {
            if (a[0] != b[0]) { // Compare distance
                return Integer.compare(a[0], b[0]);
            }
            if (a[1] != b[1]) { // Compare price
                return Integer.compare(a[1], b[1]);
            }
            if (a[2] != b[2]) { // Compare row
                return Integer.compare(a[2], b[2]);
            }
            return Integer.compare(a[3], b[3]); // Compare column
        });

        // Collect the top k items
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, items.size()); i++) {
            int[] item = items.get(i);
            result.add(Arrays.asList(item[2], item[3])); // Add only row and col
        }

        return result;
    }
}

// Time Complexity: O(M*N log(M*N))
// Space Complexity: O(M*N)