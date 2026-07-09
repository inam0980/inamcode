class Solution {
    public long[] maximumSegmentSum(int[] nums, int[] removeQueries) {
        int n = nums.length;
        long[] ans = new long[n];
        
        // Use a boolean array to mark elements that are "active" (not removed yet)
        // We process queries in reverse, so initially all elements are considered removed.
        boolean[] active = new boolean[n];
        
        // A Disjoint Set Union (DSU) structure to manage segments
        // parent[i] stores the parent of element i
        // sum[i] stores the sum of the segment rooted at i
        int[] parent = new int[n];
        long[] sum = new long[n];
        
        // Initialize DSU: each element is its own parent, sum is 0 (as elements are initially removed)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            sum[i] = 0; // Will be updated when an element is "added back"
        }
        
        // Keep track of the maximum segment sum found so far
        long maxSegmentSum = 0;

        // Iterate through removeQueries in reverse order
        // This simulates adding elements back one by one
        for (int i = n - 1; i >= 0; i--) {
            // Store the current maxSegmentSum for the current query (which is actually the previous state)
            ans[i] = maxSegmentSum; 
            
            // Get the index of the element to "add back"
            int idxToAdd = removeQueries[i];
            
            // Mark this element as active and update its sum
            active[idxToAdd] = true;
            sum[idxToAdd] = nums[idxToAdd];
            
            // Update maxSegmentSum with the sum of the newly added element itself
            maxSegmentSum = Math.max(maxSegmentSum, sum[idxToAdd]);
            
            // Check and merge with left neighbor if active
            if (idxToAdd > 0 && active[idxToAdd - 1]) {
                maxSegmentSum = Math.max(maxSegmentSum, union(parent, sum, idxToAdd, idxToAdd - 1));
            }
            
            // Check and merge with right neighbor if active
            if (idxToAdd < n - 1 && active[idxToAdd + 1]) {
                maxSegmentSum = Math.max(maxSegmentSum, union(parent, sum, idxToAdd, idxToAdd + 1));
            }
        }
        
        return ans;
    }

    // Find operation with path compression
    private int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent, parent[i]);
    }

    // Union operation by rank/size (here, just merging)
    // Returns the sum of the new merged segment
    private long union(int[] parent, long[] sum, int i, int j) {
        int rootI = find(parent, i);
        int rootJ = find(parent, j);

        if (rootI != rootJ) {
            // Merge rootJ into rootI (arbitrary choice)
            parent[rootJ] = rootI;
            sum[rootI] += sum[rootJ];
            return sum[rootI];
        }
        return sum[rootI]; // Already in the same set, return its sum
    }
}

// Time Complexity: O(N * alpha(N)), where alpha is the inverse Ackermann function, which is practically constant.
// This is because each DSU operation (find and union) takes nearly constant time on average.
// We perform N queries, and for each query, we do a constant number of DSU operations.
// Space Complexity: O(N) for parent, sum, and active arrays.