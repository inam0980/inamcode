class Solution {
    public int minScore(int n, int[][] roads) {
        // Adjacency list to store graph: city -> list of {neighbor, distance}
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        // Build the graph
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int d = road[2];
            adj.get(u).add(new int[]{v, d});
            adj.get(v).add(new int[]{u, d});
        }

        // Queue for BFS traversal
        Queue<Integer> q = new LinkedList<>();
        // Set to keep track of visited cities
        boolean[] visited = new boolean[n + 1];
        // Initialize minScore with a large value
        int minScore = Integer.MAX_VALUE;

        // Start BFS from city 1
        q.offer(1);
        visited[1] = true;

        // Perform BFS
        while (!q.isEmpty()) {
            int u = q.poll();

            // Iterate over all neighbors of the current city
            for (int[] neighborInfo : adj.get(u)) {
                int v = neighborInfo[0];
                int d = neighborInfo[1];

                // Update minScore with the minimum distance encountered so far
                minScore = Math.min(minScore, d);

                // If neighbor not visited, mark as visited and add to queue
                if (!visited[v]) {
                    visited[v] = true;
                    q.offer(v);
                }
            }
        }

        return minScore;
    }
}
// Time Complexity: O(N + M) where N is the number of cities and M is the number of roads.
// Building the adjacency list takes O(M). BFS visits each city and each road at most once.
// Space Complexity: O(N + M) for the adjacency list and O(N) for the visited array and queue.