class Solution {
    public int reachableNodes(int[][] edges, int maxMoves, int n) {
        // Adjacency list for the original graph, storing {neighbor, subdivision_count}
        // The cost to traverse an edge (u, v) is cnt + 1, where cnt is the number of subdivisions.
        // This is because we pass through cnt new nodes and then reach v.
        List<int[]>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        // Map to store how many nodes are reachable on each original edge (u, v)
        // Key: (min(u,v) * n + max(u,v)) to uniquely identify an edge
        // Value: number of reachable subdivided nodes on this edge
        Map<Integer, Integer> edgeReachableCount = new HashMap<>();

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int cnt = edge[2];
            adj[u].add(new int[]{v, cnt});
            adj[v].add(new int[]{u, cnt});
        }

        // Dijkstra's algorithm to find shortest paths from node 0
        // dist[i] stores the minimum moves to reach original node i
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;

        // Priority queue stores {moves_taken, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0}); // Start from node 0 with 0 moves

        int reachableNodesCount = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int d = current[0]; // Moves to reach current_node
            int u = current[1]; // Current original node

            // If we've already found a shorter path to u, skip
            if (d > dist[u]) {
                continue;
            }

            // This original node 'u' is reachable
            reachableNodesCount++;

            // Explore neighbors
            for (int[] neighbor : adj[u]) {
                int v = neighbor[0];
                int cnt = neighbor[1]; // Number of subdivided nodes on edge (u, v)

                // Cost to traverse the edge (u, v) is cnt + 1
                int cost = cnt + 1;

                // If we can reach 'v' with fewer moves
                if (d + cost < dist[v]) {
                    dist[v] = d + cost;
                    pq.offer(new int[]{dist[v], v});
                }

                // Calculate how many subdivided nodes on edge (u, v) are reachable from 'u'
                // Moves remaining from 'u' is maxMoves - d
                // We can reach min(cnt, maxMoves - d) new nodes from 'u' towards 'v'
                int reachableFromU = Math.min(cnt, maxMoves - d);

                // Store the maximum number of reachable nodes on this edge from either end
                int edgeKey = Math.min(u, v) * n + Math.max(u, v);
                edgeReachableCount.put(edgeKey, Math.max(edgeReachableCount.getOrDefault(edgeKey, 0), reachableFromU));
            }
        }

        // Add reachable subdivided nodes
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int cnt = edge[2];

            int edgeKey = Math.min(u, v) * n + Math.max(u, v);
            
            // Nodes reachable from u towards v
            int reachableFromU = (dist[u] <= maxMoves) ? Math.min(cnt, maxMoves - dist[u]) : 0;
            // Nodes reachable from v towards u
            int reachableFromV = (dist[v] <= maxMoves) ? Math.min(cnt, maxMoves - dist[v]) : 0;

            // The total unique reachable subdivided nodes on this edge is the sum,
            // capped by the total number of subdivisions (cnt).
            reachableNodesCount += Math.min(cnt, reachableFromU + reachableFromV);
        }

        return reachableNodesCount;
    }
}
// Time Complexity: O(E log V) where V is n and E is edges.length. Dijkstra's dominates.
// Space Complexity: O(V + E) for adjacency list, dist array, priority queue, and edgeReachableCount map.