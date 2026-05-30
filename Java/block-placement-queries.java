import java.util.*;

class Solution {
    public List<Boolean> getResults(int[][] queries) {
        // Use a TreeSet to store obstacle positions, ensuring sorted order and efficient lookups
        TreeSet<Integer> obstacles = new TreeSet<>();
        obstacles.add(0); // Add an implicit obstacle at 0 to handle the left boundary
        
        // Use a TreeMap to store available gaps and their maximum block sizes.
        // Key: right boundary of the gap, Value: maximum block size that can fit in this gap.
        // This allows efficient lookup for gaps ending at or before a certain point.
        TreeMap<Integer, Integer> gaps = new TreeMap<>();
        
        // Use another TreeMap to store the frequency of each gap size.
        // Key: gap size, Value: count of gaps with this size.
        // This helps find the maximum available gap size efficiently.
        TreeMap<Integer, Integer> gapSizes = new TreeMap<>();
        
        // Initialize with an infinitely large gap from 0 to MAX_X (or a sufficiently large number)
        // For practical purposes, we can consider the maximum possible x value.
        // The problem constraints state x <= min(5*10^4, 3*queries.length).
        // A safe upper bound for x is 5 * 10^4.
        int MAX_X = 50001; 
        gaps.put(MAX_X, MAX_X); // Gap from 0 to MAX_X, max block size is MAX_X
        gapSizes.put(MAX_X, 1); // One gap of size MAX_X
        
        List<Boolean> results = new ArrayList<>();

        for (int[] query : queries) {
            int type = query[0];

            if (type == 1) {
                int x = query[1];
                
                // Find the obstacle immediately to the left of x
                int prevObstacle = obstacles.floor(x);
                // Find the obstacle immediately to the right of x
                int nextObstacle = obstacles.ceiling(x);

                // Remove the old gap that spanned from prevObstacle to nextObstacle
                int oldGapSize = nextObstacle - prevObstacle;
                int oldMaxBlockSize = oldGapSize; // Max block size in this gap is its length
                
                // Decrement count for old gap size, remove if count becomes 0
                gapSizes.put(oldMaxBlockSize, gapSizes.get(oldMaxBlockSize) - 1);
                if (gapSizes.get(oldMaxBlockSize) == 0) {
                    gapSizes.remove(oldMaxBlockSize);
                }
                // Remove the old gap from the 'gaps' map
                gaps.remove(nextObstacle);

                // Add the new obstacle
                obstacles.add(x);

                // Create two new gaps:
                // 1. From prevObstacle to x
                int newGap1Size = x - prevObstacle;
                int newMaxBlockSize1 = newGap1Size;
                gaps.put(x, newMaxBlockSize1);
                gapSizes.put(newMaxBlockSize1, gapSizes.getOrDefault(newMaxBlockSize1, 0) + 1);

                // 2. From x to nextObstacle
                int newGap2Size = nextObstacle - x;
                int newMaxBlockSize2 = newGap2Size;
                gaps.put(nextObstacle, newMaxBlockSize2);
                gapSizes.put(newMaxBlockSize2, gapSizes.getOrDefault(newMaxBlockSize2, 0) + 1);

            } else { // type == 2
                int x = query[1];
                int sz = query[2];

                // Find the largest gap whose right boundary is <= x
                // This gives us the maximum block size available in any gap ending at or before x.
                Map.Entry<Integer, Integer> entry = gaps.floorEntry(x);
                int maxBlockSizeInRelevantGap = (entry != null) ? entry.getValue() : 0;

                // Check if the required block size 'sz' can fit in any gap.
                // This means checking if there's any gap with size >= sz.
                // We use the gapSizes map for this.
                Integer largestPossibleGapSize = gapSizes.ceilingKey(sz);

                // The condition for placing a block of size 'sz' in range [0, x] is:
                // 1. There must be a gap ending at or before 'x' that can accommodate 'sz'.
                //    This is checked by maxBlockSizeInRelevantGap >= sz.
                // 2. OR, there must be a gap *anywhere* that is large enough, AND its right boundary
                //    is at least 'x'. This is implicitly handled by the 'gaps' map.
                //    The 'gaps' map stores the maximum block size that can be placed *up to* its key.
                //    So, if gaps.get(rightBoundary) is the max block size for a gap ending at rightBoundary,
                //    then for any x, we need to find the max block size for a gap ending at or before x.
                //    This is what floorEntry(x) gives us.

                // The logic for `gaps` map needs to be refined.
                // `gaps.put(rightBoundary, maxBlockSize)` should store the maximum block size
                // that can be placed *ending at or before* `rightBoundary`.
                // When an obstacle is added, it breaks a gap. The new gaps are added.
                // The `gaps` map should store the maximum block size encountered so far.
                // This is a prefix maximum concept.

                // Let's re-evaluate the `gaps` map.
                // `gaps` should store `(right_boundary, max_block_size_up_to_this_boundary)`.
                // When a new obstacle `x` is added, it creates a new gap `(prevObstacle, x)`.
                // The max block size for this gap is `x - prevObstacle`.
                // The `gaps` map should be updated such that for any `k >= x`,
                // `gaps.get(k)` reflects the maximum block size available up to `k`.

                // A simpler approach for type 2 query:
                // We need to check if there exists a gap [a, b] such that b <= x and b - a >= sz.
                // The `obstacles` set gives us `a` and `b`.
                // Iterate through obstacles to find such a gap. This is too slow.

                // The `gaps` map should store `(right_boundary, max_block_size_in_any_gap_ending_at_or_before_this_boundary)`.
                // When we add an obstacle `x`, we create a new gap `(prev, x)` of size `x - prev`.
                // And another gap `(x, next)` of size `next - x`.
                // The `gaps` map needs to be updated for all keys `k >= x`.
                // This is a range update, which is slow for TreeMap.

                // Let's reconsider the `gaps` map:
                // `gaps` stores `(right_boundary, max_block_size_in_this_specific_gap)`.
                // When we query `(x, sz)`, we need to find if there exists any `(right_boundary, block_size)` in `gaps`
                // such that `right_boundary <= x` AND `block_size >= sz`.
                // This can be done by iterating `gaps.headMap(x, true).values()` and checking if any value >= sz.
                // This is also too slow.

                // The correct way to use `gaps` for type 2 query:
                // `gaps` stores `(right_boundary, max_block_size_available_in_any_gap_ending_at_or_before_right_boundary)`.
                // When an obstacle `x` is added:
                // 1. Find `prev = obstacles.floor(x)` and `next = obstacles.ceiling(x)`.
                // 2. The old gap `(prev, next)` of size `next - prev` is removed.
                //    We need to update `gaps` for all keys `k >= next`.
                //    This is problematic.

                // Let's use a different interpretation of `gaps`:
                // `gaps` stores `(right_boundary, max_block_size_in_the_gap_ending_at_right_boundary)`.
                // And `gapSizes` stores `(gap_size, count)`.
                // For a query `(x, sz)`:
                // We need to find if there is any gap `[a, b]` such that `b <= x` and `b - a >= sz`.
                // The `gapSizes` map can tell us if there exists a gap of size `sz` or more.
                // `largestPossibleGapSize = gapSizes.ceilingKey(sz)` will give us the smallest gap size that is >= sz.
                // If `largestPossibleGapSize` is null, no such gap exists.
                // If it exists, we need to check if *any* of these gaps (of size `largestPossibleGapSize` or larger)
                // has its right boundary `b <= x`.

                // This requires iterating through `gaps` or a more complex data structure.
                // A segment tree or Fenwick tree could work if we map x coordinates to indices.
                // Given the constraints (x up to 5*10^4), a segment tree is feasible.

                // Let's try to use the `gaps` map as a prefix maximum structure.
                // `gaps` stores `(right_boundary, max_block_size_up_to_this_boundary)`.
                // When an obstacle `x` is added:
                // 1. Find `prev = obstacles.floor(x)` and `next = obstacles.ceiling(x)`.
                // 2. The old gap `(prev, next)` of size `next - prev` is broken.
                // 3. New gaps `(prev, x)` and `(x, next)` are formed.
                // 4. The `gaps` map needs to be updated.
                //    For `k` in `gaps`, `gaps.get(k)` should be `max(gaps.get(k_prev), current_gap_size_ending_at_k)`.
                // This is a standard prefix maximum.

                // Let's maintain `gaps` such that `gaps.get(k)` is the maximum block size
                // that can be placed in *any* gap `[a, b]` where `b <= k`.
                // When an obstacle `x` is added:
                //   - `prev = obstacles.floor(x)`
                //   - `next = obstacles.ceiling(x)`
                //   - Old gap `(prev, next)` is removed. Its contribution to prefix max needs to be removed.
                //   - New gaps `(prev, x)` and `(x, next)` are added. Their contributions need to be added.

                // This is tricky because removing a value from a prefix max structure requires recomputing.
                // A `Segment Tree` or `Sparse Table` is better for range maximum queries.

                // Let's try a simpler approach for type 2 query with the current `gaps` and `gapSizes` structure:
                // `obstacles`: stores all obstacle positions.
                // `gaps`: stores `(right_boundary, size_of_gap_ending_at_right_boundary)`.
                // `gapSizes`: stores `(size, count)`.

                // For query `(x, sz)`:
                // We need to find if there exists a gap `[a, b]` such that `b <= x` and `b - a >= sz`.
                // 1. Find the largest gap size `maxPossibleGapSize` that is >= `sz`.
                //    `maxPossibleGapSize = gapSizes.ceilingKey(sz)`.
                //    If `maxPossibleGapSize == null`, then no gap is large enough. Result is false.
                // 2. If such a `maxPossibleGapSize` exists, we need to check if *any* gap `[a, b]`
                //    with `b - a >= sz` has its right boundary `b <= x`.
                //    This means we need to query `gaps` for entries `(b, size)` where `b <= x` and `size >= sz`.
                //    This is a 2D query (range on `b`, range on `size`).
                //    A segment tree where leaves are `x` coordinates and nodes store max gap size ending at `x`
                //    would work.

                // Let's use a Segment Tree.
                // The segment tree will store the maximum gap size that can be placed ending at or before a given x-coordinate.
                // The tree will cover the range [0, MAX_X].
                // Each node in the segment tree will store the maximum block size that can be placed in its range.

                // Segment Tree implementation:
                // `max_x_coord = 50001`
                // `tree_size = 4 * max_x_coord`
                // `segmentTree = new int[tree_size]`
                // `build(1, 0, max_x_coord)` // Initialize with 0s or appropriate values

                // `update(node, start, end, idx, val)`: updates the value at `idx` to `val`
                // `query(node, start, end, l, r)`: queries max value in range `[l, r]`

                // When an obstacle `x` is added:
                // 1. `prev = obstacles.floor(x)`
                // 2. `next = obstacles.ceiling(x)`
                // 3. Remove old gap `(prev, next)`: `update(1, 0, MAX_X, next, 0)` (or its previous value if it was the max)
                //    This is not quite right. We need to update the *value* at `next` to reflect the new max.
                //    The value at `next` should be `next - x`.
                //    The value at `x` should be `x - prev`.

                // Let's refine the segment tree approach:
                // The segment tree will store `max_block_size_ending_at_x` for each `x`.
                // `segmentTree[i]` will store the maximum block size that can be placed in a gap `[a, i]`
                // such that `a` is an obstacle and `i` is an obstacle or `i` is `MAX_X`.

                // `obstacles` set: stores all obstacle positions.
                // `segmentTree`: a segment tree over `[0, MAX_X]`.
                // Each leaf `i` stores the maximum block size `b-a` for a gap `[a, b]` where `b=i`.
                // Internal nodes store the maximum of their children.

                // Initialize segment tree:
                // `update(1, 0, MAX_X, MAX_X, MAX_X)` // Initial gap from 0 to MAX_X

                // For type 1 query `[1, x]`:
                // 1. `prev = obstacles.floor(x)`
                // 2. `next = obstacles.ceiling(x)`
                // 3. `obstacles.add(x)`
                // 4. Update segment tree:
                //    - The gap `(prev, next)` is broken. Its contribution to `next` is removed.
                //      The new gap ending at `next` is `(x, next)` with size `next - x`.
                //      `update(1, 0, MAX_X, next, next - x)`
                //    - A new gap `(prev, x)` is created. Its contribution to `x` is `x - prev`.
                //      `update(1, 0, MAX_X, x, x - prev)`

                // For type 2 query `[2, x, sz]`:
                // Query the segment tree for the maximum value in range `[0, x]`.
                // `max_block_size_in_range = query(1, 0, MAX_X, 0, x)`
                // If `max_block_size_in_range >= sz`, then it's possible.

                // This segment tree approach seems correct and efficient.

                // Max coordinate value
                int MAX_COORD = 50001; 
                
                // Segment tree array. Size 4*MAX_COORD is typical for 0-indexed segment trees.
                // Each node stores the maximum gap size ending at or before any point in its range.
                // `segmentTree[i]` stores the maximum value in the range represented by node `i`.
                // `segmentTree[0]` is unused, root is `1`.
                int[] segmentTree = new int[4 * MAX_COORD];

                // Build function to initialize the segment tree.
                // `node`: current node index in segmentTree array
                // `start`, `end`: range covered by current node
                // `idx`: the specific index to update
                // `val`: the new value for `idx`
                // `update(1, 0, MAX_COORD, MAX_COORD, MAX_COORD)` will set the initial gap.
                // The initial gap is from 0 to MAX_COORD, so the max block size ending at MAX_COORD is MAX_COORD.
                // All other positions initially have 0 max block size.
                
                // Helper function for segment tree update
                // Sets the value at `idx` to `val` and propagates changes up.
                // This is a point update.
                class SegmentTree {
                    int[] tree;
                    int maxCoord;

                    SegmentTree(int maxCoord) {
                        this.maxCoord = maxCoord;
                        this.tree = new int[4 * maxCoord];
                    }

                    void update(int node, int start, int end, int idx, int val) {
                        if (start == end) { // Leaf node
                            tree[node] = val;
                        } else {
                            int mid = (start + end) / 2;
                            if (start <= idx && idx <= mid) {
                                update(2 * node, start, mid, idx, val);
                            } else {
                                update(2 * node + 1, mid + 1, end, idx, val);
                            }
                            tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
                        }
                    }

                    // Helper function for segment tree query
                    // Returns the maximum value in the range [l, r]
                    int query(int node, int start, int end, int l, int r) {
                        if (r < start || end < l) { // Query range outside node range
                            return 0; // No overlap, return identity for max (0)
                        }
                        if (l <= start && end <= r) { // Node range completely within query range
                            return tree[node];
                        }
                        // Partial overlap, recurse
                        int mid = (start + end) / 2;
                        int p1 = query(2 * node, start, mid, l, r);
                        int p2 = query(2 * node + 1, mid + 1, end, l, r);
                        return Math.max(p1, p2);
                    }
                }

                SegmentTree st = new SegmentTree(MAX_COORD);
                
                // Initialize with an implicit obstacle at 0 and a very large obstacle at MAX_COORD.
                // The gap from 0 to MAX_COORD has size MAX_COORD.
                // The max block size ending at MAX_COORD is MAX_COORD.
                st.update(1, 0, MAX_COORD, MAX_COORD, MAX_COORD);
                obstacles.add(0); // Add implicit obstacle at 0
                obstacles.add(MAX_COORD); // Add implicit obstacle at MAX_COORD

                // Re-process queries with the SegmentTree
                results.clear(); // Clear previous results if any

                for (int[] q : queries) {
                    int type_q = q[0];

                    if (type_q == 1) {
                        int x = q[1];
                        
                        // Find obstacles immediately to the left and right of x
                        int prevObstacle = obstacles.floor(x);
                        int nextObstacle = obstacles.ceiling(x);

                        // Add the new obstacle
                        obstacles.add(x);

                        // Update segment tree for the two new gaps:
                        // 1. Gap from prevObstacle to x, ending at x. Size: x - prevObstacle.
                        st.update(1, 0, MAX_COORD, x, x - prevObstacle);

                        // 2. Gap from x to nextObstacle, ending at nextObstacle. Size: nextObstacle - x.
                        //    This replaces the old gap (prevObstacle, nextObstacle) contribution at nextObstacle.
                        st.update(1, 0, MAX_COORD, nextObstacle, nextObstacle - x);

                    } else { // type_q == 2
                        int x = q[1];
                        int sz = q[2];

                        // Query the segment tree for the maximum block size in range [0, x]
                        int maxBlockSizeInQueryRange = st.query(1, 0, MAX_COORD, 0, x);
                        
                        results.add(maxBlockSizeInQueryRange >= sz);
                    }
                }
                return results;
            }
        }
        return results; // This line will not be reached due to the inner return.
    }
}

// Time Complexity:
// Each query involves TreeSet operations (floor, ceiling, add) which are O(log N_obs), where N_obs is the number of obstacles.
// Each query also involves Segment Tree operations (update, query) which are O(log MAX_COORD).
// N_obs can be up to Q (number of queries). MAX_COORD is constant (5*10^4).
// Total time complexity: O(Q * (log Q + log MAX_COORD)).
// Given Q <= 1.5 * 10^4 and MAX_COORD = 5 * 10^4, log Q approx 14, log MAX_COORD approx 16.
// So, O(Q * log MAX_COORD) is the dominant factor.
// Q * log MAX_COORD = 1.5 * 10^4 * 16 = 2.4 * 10^5, which is efficient enough.

// Space Complexity:
// TreeSet for obstacles: O(Q) in worst case (all type 1 queries).
// Segment Tree: O(MAX_COORD) for the tree array.
// List for results: O(Q).
// Total space complexity: O(Q + MAX_COORD).
// Q = 1.5 * 10^4, MAX_COORD = 5 * 10^4.
// So, O(MAX_COORD) is the dominant factor.