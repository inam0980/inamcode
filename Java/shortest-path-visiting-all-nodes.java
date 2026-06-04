class Solution {
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        // If there's only one node, path length is 0
        if (n == 1) {
            return 0;
        }

        // State for BFS: {node, mask, distance}
        // node: current node
        // mask: bitmask representing visited nodes
        // distance: path length to reach this state
        Queue<int[]> queue = new LinkedList<>();
        // Set to keep track of visited states to avoid cycles and redundant computations
        // A state is defined by (node, mask)
        Set<String> visited = new HashSet<>();

        // Initialize BFS: Start from every node
        // For each starting node 'i', the initial mask will have the i-th bit set
        // and distance will be 0.
        for (int i = 0; i < n; i++) {
            int initialMask = (1 << i); // Set i-th bit
            queue.offer(new int[]{i, initialMask, 0});
            visited.add(i + "-" + initialMask);
        }

        // The target mask where all bits are set, indicating all nodes have been visited
        int allVisitedMask = (1 << n) - 1;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int u = current[0];
            int mask = current[1];
            int dist = current[2];

            // If all nodes have been visited, we found the shortest path
            if (mask == allVisitedMask) {
                return dist;
            }

            // Explore neighbors
            for (int v : graph[u]) {
                int newMask = mask | (1 << v); // Set v-th bit in the mask
                String newState = v + "-" + newMask;

                // If this state (node, mask) has not been visited before
                if (!visited.contains(newState)) {
                    visited.add(newState);
                    queue.offer(new int[]{v, newMask, dist + 1});
                }
            }
        }

        // This line should theoretically not be reached because the graph is connected
        // and a path visiting all nodes always exists.
        return -1;
    }
}

// Time Complexity: O(N * 2^N)
// N is the number of nodes. There are N possible starting nodes.
// Each state in BFS is defined by (current_node, visited_mask).
// There are N possible current nodes and 2^N possible masks.
// So, there are N * 2^N possible states.
// For each state, we iterate through its neighbors (at most N neighbors).
// Therefore, the total time complexity is O(N * 2^N * N) = O(N^2 * 2^N).
// However, the number of edges is at most N*(N-1)/2.
// A more precise analysis considers that each edge (u,v) is processed once for each mask.
// The number of states is N * 2^N. Each state is added to the queue and processed once.
// When processing a state (u, mask), we iterate over its neighbors.
// The total number of edges in the graph is E. In the worst case, E is O(N^2).
// So, the total operations would be roughly O(E * 2^N) or O(N * 2^N) if we consider the number of states.
// Given N <= 12, N * 2^N is roughly 12 * 4096 = 49152.
// N^2 * 2^N is roughly 144 * 4096 = 589824. This is feasible.

// Space Complexity: O(N * 2^N)
// The queue can store up to N * 2^N states in the worst case.
// The visited set also stores up to N * 2^N states.
// Each state is represented by an int array of size 3 or a string.