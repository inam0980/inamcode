class Solution {
    public int minReorder(int n, int[][] connections) {
        // Adjacency list to represent the graph.
        // For each node, store pairs: {neighbor, direction_flag}
        // direction_flag = 1 if original edge is (current -> neighbor)
        // direction_flag = 0 if original edge is (neighbor -> current)
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        // Build the adjacency list
        for (int[] conn : connections) {
            int u = conn[0];
            int v = conn[1];
            adj.get(u).add(new int[]{v, 1}); // Original edge u -> v
            adj.get(v).add(new int[]{u, 0}); // Original edge v <- u (represented as u -> v with flag 0)
        }

        int reorders = 0;
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();

        // Start BFS from city 0
        queue.offer(0);
        visited[0] = true;

        while (!queue.isEmpty()) {
            int currentCity = queue.poll();

            // Iterate through all neighbors of the current city
            for (int[] neighborInfo : adj.get(currentCity)) {
                int neighbor = neighborInfo[0];
                int directionFlag = neighborInfo[1];

                if (!visited[neighbor]) {
                    // If the original edge was currentCity -> neighbor,
                    // it needs to be reordered to neighbor -> currentCity
                    if (directionFlag == 1) {
                        reorders++;
                    }
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }

        return reorders;
    }
}

// Time complexity: O(N + M) where N is the number of cities and M is the number of connections.
// Building the adjacency list takes O(M). BFS visits each node and edge once, taking O(N + M).
// Space complexity: O(N + M) for the adjacency list and the queue.