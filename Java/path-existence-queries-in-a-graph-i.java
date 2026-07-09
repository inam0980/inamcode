class Solution {
    private int[] parent;
    private int[] rank;

    // Initializes the Disjoint Set Union (DSU) structure
    private void initDSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // Each node is its own parent initially
            rank[i] = 0;   // Rank (height) of each tree is 0
        }
    }

    // Finds the representative (root) of the set containing element i
    // with path compression
    private int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]); // Path compression
    }

    // Unites the sets containing elements i and j
    // with union by rank
    private void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            // Union by rank: attach smaller rank tree under root of higher rank tree
            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
            } else {
                parent[rootJ] = rootI; // If ranks are same, pick one as root
                rank[rootI]++;         // Increment rank of the new root
            }
        }
    }

    public boolean[] pathExistenceQueries(int n, int[] nums, int maxDiff, int[][] queries) {
        initDSU(n); // Initialize DSU for n nodes

        // Iterate through adjacent nodes to form initial edges
        // Since nums is sorted, we only need to check adjacent elements
        // and elements within maxDiff range.
        // A more robust approach for general sorted arrays is to use two pointers
        // or binary search to find all valid j for each i.
        // However, for this problem, the constraint |nums[i] - nums[j]| <= maxDiff
        // implies that if nums[i] and nums[j] are far apart, they won't form an edge.
        // Given nums is sorted, we can iterate i from 0 to n-1.
        // For each i, we need to find all j > i such that nums[j] - nums[i] <= maxDiff.
        // A two-pointer approach is efficient here.
        int right = 0;
        for (int left = 0; left < n; left++) {
            // Advance 'right' pointer to find all nodes 'j' such that nums[j] - nums[left] <= maxDiff
            // and union 'left' with all such 'j's.
            // Since we are building connected components, we only need to union 'left' with 'left+1',
            // and then 'left+1' with 'left+2', etc., as long as the difference condition holds.
            // This effectively connects all nodes in a contiguous block that satisfy the condition.
            while (right < n && nums[right] - nums[left] <= maxDiff) {
                // Union 'left' with 'right' if they are not already connected.
                // More precisely, union 'right' with 'right-1' if 'right-1' is within the window.
                // This connects all nodes in the current window [left, right] into one component.
                if (right > 0 && nums[right] - nums[right - 1] <= maxDiff) {
                    union(right, right - 1);
                }
                right++;
            }
            // After the loop, 'right' is the first index where nums[right] - nums[left] > maxDiff.
            // All nodes from 'left' to 'right-1' are potentially connected.
            // The DSU union operations within the while loop ensure this.
        }


        boolean[] results = new boolean[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            // Check if u and v belong to the same connected component
            results[i] = (find(u) == find(v));
        }

        return results;
    }
}
// Time Complexity: O(N + Q * alpha(N)) where N is the number of nodes and Q is the number of queries.
// The DSU initialization is O(N).
// Building the graph (union operations): The two-pointer approach iterates 'left' from 0 to N-1, and 'right' from 0 to N-1.
// Each node is visited by 'left' and 'right' at most once. Each union operation takes O(alpha(N)) time on average,
// where alpha is the inverse Ackermann function, which is practically constant. So, O(N * alpha(N)).
// Processing queries: Each query involves two find operations, taking O(alpha(N)) time. So, O(Q * alpha(N)).
// Overall: O(N * alpha(N) + Q * alpha(N)) which simplifies to O((N + Q) * alpha(N)).
// Space Complexity: O(N) for parent and rank arrays in DSU.