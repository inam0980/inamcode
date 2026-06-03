class Solution {
    public boolean canReachCorner(int xCorner, int yCorner, int[][] circles) {
        int n = circles.length;

        // Define 4 virtual nodes representing the boundaries:
        // 0: bottom edge (y=0)
        // 1: left edge (x=0)
        // 2: top edge (y=yCorner)
        // 3: right edge (x=xCorner)
        // Circles are indexed from 4 to 4 + n - 1

        // Adjacency list for the graph
        // Size is n + 4 (n circles + 4 boundaries)
        // We use indices 0 to n+3.
        // 0: bottom, 1: left, 2: top, 3: right
        // 4 to n+3: circles[i-4]
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n + 4; i++) {
            adj.add(new ArrayList<>());
        }

        // Helper function to check if two circles overlap
        // or if a circle overlaps with a boundary
        // Distance squared between (x1, y1) and (x2, y2)
        long distSq(long x1, long y1, long x2, long y2) {
            return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        }

        // Connect circles to each other
        for (int i = 0; i < n; i++) {
            long x1 = circles[i][0];
            long y1 = circles[i][1];
            long r1 = circles[i][2];

            for (int j = i + 1; j < n; j++) {
                long x2 = circles[j][0];
                long y2 = circles[j][1];
                long r2 = circles[j][2];

                // If distance between centers is less than or equal to sum of radii, they overlap
                if (distSq(x1, y1, x2, y2) <= (r1 + r2) * (r1 + r2)) {
                    adj.get(i + 4).add(j + 4); // Circle i is node i+4
                    adj.get(j + 4).add(i + 4); // Circle j is node j+4
                }
            }

            // Connect circles to boundaries
            // Bottom edge (y=0)
            if (y1 - r1 <= 0) {
                adj.get(i + 4).add(0);
                adj.get(0).add(i + 4);
            }
            // Left edge (x=0)
            if (x1 - r1 <= 0) {
                adj.get(i + 4).add(1);
                adj.get(1).add(i + 4);
            }
            // Top edge (y=yCorner)
            if (y1 + r1 >= yCorner) {
                adj.get(i + 4).add(2);
                adj.get(2).add(i + 4);
            }
            // Right edge (x=xCorner)
            if (x1 + r1 >= xCorner) {
                adj.get(i + 4).add(3);
                adj.get(3).add(i + 4);
            }
        }

        // BFS/DFS to find connected components
        boolean[] visited = new boolean[n + 4];
        Queue<Integer> q = new LinkedList<>();

        // Start BFS from bottom (0) and left (1) boundaries
        // If bottom is blocked, add 0 to queue
        // If left is blocked, add 1 to queue
        // A boundary is blocked if any circle touches it.
        // We are looking for a path from (0,0) to (xCorner, yCorner).
        // This means we need to avoid paths that connect bottom to top,
        // or left to right.
        // If (0,0) is covered by a circle, it's impossible.
        // If (xCorner, yCorner) is covered by a circle, it's impossible.
        // The problem statement implies the path must lie strictly inside the rectangle,
        // touching only corners. This means circles cannot cover the corners.
        // However, the examples show circles can be at (1,1) with radius 2, covering (0,0).
        // Let's assume the problem means the path itself cannot be inside a circle.
        // The path starts at (0,0) and ends at (xCorner, yCorner).
        // If a circle covers (0,0) or (xCorner, yCorner), it's impossible.
        // Check if (0,0) is covered by any circle
        for (int i = 0; i < n; i++) {
            long x = circles[i][0];
            long y = circles[i][1];
            long r = circles[i][2];
            if (distSq(x, y, 0, 0) <= r * r) {
                return false; // (0,0) is covered
            }
            if (distSq(x, y, xCorner, yCorner) <= r * r) {
                return false; // (xCorner, yCorner) is covered
            }
        }

        // We want to find if bottom-left corner is "separated" from top-right corner.
        // This means checking if there's a path of overlapping circles
        // that connects the bottom edge to the top edge, OR
        // that connects the left edge to the right edge.
        // If such a path exists, then the corner is not reachable.

        // Perform BFS from all nodes that are connected to the bottom edge (0) or left edge (1).
        // These are the "start" nodes for our blocking path.
        for (int i = 0; i < n + 4; i++) {
            if (i == 0 || i == 1) { // If it's a boundary node (bottom or left)
                q.offer(i);
                visited[i] = true;
            } else if (i >= 4) { // If it's a circle
                long x = circles[i - 4][0];
                long y = circles[i - 4][1];
                long r = circles[i - 4][2];
                // If a circle touches bottom or left, it's a starting point for blocking
                if (y - r <= 0 || x - r <= 0) {
                    q.offer(i);
                    visited[i] = true;
                }
            }
        }

        boolean reachesTop = false;
        boolean reachesRight = false;

        while (!q.isEmpty()) {
            int u = q.poll();

            if (u == 2) reachesTop = true;    // Reached top edge
            if (u == 3) reachesRight = true;  // Reached right edge

            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.offer(v);
                }
            }
        }

        // If bottom-left is connected to top-right, it means a path exists
        // that blocks the corner.
        // The path is blocked if:
        // 1. Bottom edge is connected to Top edge (reachesTop is true)
        // 2. Left edge is connected to Right edge (reachesRight is true)
        // If either of these conditions is true, then the path from (0,0) to (xCorner, yCorner)
        // is blocked.
        return !(reachesTop && reachesRight);
    }
}
/*
Time Complexity: O(N^2) where N is the number of circles.
Building the graph involves iterating through all pairs of circles (N^2) and each circle against 4 boundaries (N).
BFS/DFS takes O(V + E) where V is N+4 and E is at most N^2. So, O(N^2).
Space Complexity: O(N^2) for the adjacency list in the worst case (all circles connected to each other).
*/