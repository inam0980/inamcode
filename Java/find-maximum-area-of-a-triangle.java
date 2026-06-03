class Solution {
    public long maxArea(int[][] coords) {
        // Store points grouped by x-coordinate and y-coordinate
        // Using TreeMap to keep coordinates sorted, which simplifies finding min/max
        java.util.Map<Integer, java.util.TreeMap<Integer, Integer>> xMap = new java.util.TreeMap<>(); // x -> {y -> count}
        java.util.Map<Integer, java.util.TreeMap<Integer, Integer>> yMap = new java.util.TreeMap<>(); // y -> {x -> count}

        for (int[] p : coords) {
            int x = p[0];
            int y = p[1];
            xMap.computeIfAbsent(x, k -> new java.util.TreeMap<>()).put(y, 1);
            yMap.computeIfAbsent(y, k -> new java.util.TreeMap<>()).put(x, 1);
        }

        long maxDoubleArea = -1;

        // Case 1: Base parallel to x-axis (same y-coordinate)
        for (java.util.Map.Entry<Integer, java.util.TreeMap<Integer, Integer>> entryY : yMap.entrySet()) {
            int yCoord = entryY.getKey();
            java.util.TreeMap<Integer, Integer> xCoords = entryY.getValue();

            if (xCoords.size() < 2) { // Need at least two points for a base
                continue;
            }

            // Iterate through all possible pairs of points on this horizontal line
            // (x1, yCoord) and (x2, yCoord)
            for (int x1 : xCoords.keySet()) {
                for (int x2 : xCoords.keySet()) {
                    if (x1 >= x2) continue; // Ensure x1 < x2 to avoid duplicates and zero length base

                    long base = x2 - x1;
                    if (base == 0) continue; // Base cannot be zero

                    // Find a third point (x3, y3) such that y3 != yCoord
                    // The height will be |y3 - yCoord|
                    // We need to find the y3 that maximizes this height.
                    // This means finding the y3 furthest from yCoord.

                    // Check points with x-coordinate x1
                    if (xMap.containsKey(x1)) {
                        java.util.TreeMap<Integer, Integer> yValuesForX1 = xMap.get(x1);
                        if (yValuesForX1.size() > 1) { // Need at least one other y for height
                            // Find max height from yCoord using points at x1
                            if (yValuesForX1.firstKey() != yCoord) {
                                long height = Math.abs(yValuesForX1.firstKey() - yCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                            if (yValuesForX1.lastKey() != yCoord) {
                                long height = Math.abs(yValuesForX1.lastKey() - yCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                        }
                    }

                    // Check points with x-coordinate x2
                    if (xMap.containsKey(x2)) {
                        java.util.TreeMap<Integer, Integer> yValuesForX2 = xMap.get(x2);
                        if (yValuesForX2.size() > 1) { // Need at least one other y for height
                            // Find max height from yCoord using points at x2
                            if (yValuesForX2.firstKey() != yCoord) {
                                long height = Math.abs(yValuesForX2.firstKey() - yCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                            if (yValuesForX2.lastKey() != yCoord) {
                                long height = Math.abs(yValuesForX2.lastKey() - yCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                        }
                    }
                }
            }
        }

        // Case 2: Base parallel to y-axis (same x-coordinate)
        for (java.util.Map.Entry<Integer, java.util.TreeMap<Integer, Integer>> entryX : xMap.entrySet()) {
            int xCoord = entryX.getKey();
            java.util.TreeMap<Integer, Integer> yCoords = entryX.getValue();

            if (yCoords.size() < 2) { // Need at least two points for a base
                continue;
            }

            // Iterate through all possible pairs of points on this vertical line
            // (xCoord, y1) and (xCoord, y2)
            for (int y1 : yCoords.keySet()) {
                for (int y2 : yCoords.keySet()) {
                    if (y1 >= y2) continue; // Ensure y1 < y2 to avoid duplicates and zero length base

                    long base = y2 - y1;
                    if (base == 0) continue; // Base cannot be zero

                    // Find a third point (x3, y3) such that x3 != xCoord
                    // The height will be |x3 - xCoord|
                    // We need to find the x3 that maximizes this height.
                    // This means finding the x3 furthest from xCoord.

                    // Check points with y-coordinate y1
                    if (yMap.containsKey(y1)) {
                        java.util.TreeMap<Integer, Integer> xValuesForY1 = yMap.get(y1);
                        if (xValuesForY1.size() > 1) { // Need at least one other x for height
                            // Find max height from xCoord using points at y1
                            if (xValuesForY1.firstKey() != xCoord) {
                                long height = Math.abs(xValuesForY1.firstKey() - xCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                            if (xValuesForY1.lastKey() != xCoord) {
                                long height = Math.abs(xValuesForY1.lastKey() - xCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                        }
                    }

                    // Check points with y-coordinate y2
                    if (yMap.containsKey(y2)) {
                        java.util.TreeMap<Integer, Integer> xValuesForY2 = yMap.get(y2);
                        if (xValuesForY2.size() > 1) { // Need at least one other x for height
                            // Find max height from xCoord using points at y2
                            if (xValuesForY2.firstKey() != xCoord) {
                                long height = Math.abs(xValuesForY2.firstKey() - xCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                            if (xValuesForY2.lastKey() != xCoord) {
                                long height = Math.abs(xValuesForY2.lastKey() - xCoord);
                                maxDoubleArea = Math.max(maxDoubleArea, base * height);
                            }
                        }
                    }
                }
            }
        }

        return maxDoubleArea;
    }
}

// Time Complexity: O(N * (max_coords_per_line)^2) in worst case, but practically better.
// More precisely, O(N + Sum(L_x^2) + Sum(L_y^2)) where L_x is number of points on a vertical line, L_y on horizontal.
// In the worst case, all points could be on one line, leading to O(N^2).
// However, the optimization of only checking min/max for height reduces the inner loop.
// The current implementation iterates through all pairs on a line, then checks min/max for height.
// A better approach for height: for each line, find the min and max y (or x) values across all other lines.
// Let's refine the complexity for the current code.
// Building maps: O(N log N) due to TreeMap insertions.
// Iterating through yMap:
//   Outer loop: O(N_y) where N_y is number of unique y-coordinates.
//   Inner loops (x1, x2): O(L_y^2) where L_y is max points on a horizontal line.
//   Inside inner loops: O(log N) for TreeMap.firstKey/lastKey.
// Total for horizontal base: O(N_y * L_y^2 * log N).
// Total for vertical base: O(N_x * L_x^2 * log N).
// In the worst case, all N points could be on one line (e.g., x=1). Then N_y=1, L_y=N. This becomes O(N^2 log N).
// If points are distributed, e.g., sqrt(N) points on sqrt(N) lines, it's better.
// Given N <= 10^5, N^2 is too slow.
// The current approach for finding height is inefficient.
// For a base (x1, yCoord) to (x2, yCoord), the height is max(|y3 - yCoord|) for any point (x3, y3) where x3 != x1 and x3 != x2.
// This requires iterating through all other points, which is too slow.

// Let's re-evaluate the height finding.
// For a base (x1, yCoord) to (x2, yCoord), the third point (x3, y3) must have y3 != yCoord.
// The height is |y3 - yCoord|.
// To maximize this, we need to find the y3 that is furthest from yCoord.
// This y3 can come from any x-coordinate x3.
// The problem states "at least one side of this triangle is parallel to the x-axis or y-axis".
// This means the third point (x3, y3) can be (x1, y3) or (x2, y3) or (x3, yCoord).
// The current code only considers (x1, y3) or (x2, y3).
// If the third point is (x3, yCoord), then the base is (x1, yCoord) to (x3, yCoord) or (x2, yCoord) to (x3, yCoord).
// This is covered by iterating through all pairs on the same line.

// The critical part is finding the third point (x3, y3) such that y3 != yCoord.
// If the base is (x1, yCoord) and (x2, yCoord), the third point (x3, y3) must form a height.
// The height is |y3 - yCoord|.
// The problem implies that the third point must share an x-coordinate with one of the base points, OR share a y-coordinate with one of the base points.
// No, it says "at least one side of this triangle is parallel to the x-axis or y-axis".
// This means:
// 1. Base is horizontal (y1=y2). Third point (x3, y3).
//    Side 1: (x1, y1) to (x2, y1) -> parallel to x-axis. Area = 0.5 * |x2-x1| * |y3-y1|.
//    To maximize, we need max |y3-y1|.
//    This y3 can be any y-coordinate from any point (x3, y3) in the entire set, as long as x3 != x1 and x3 != x2.
//    This is the part that makes it O(N^2) if not optimized.

// Let's optimize the height finding.
// For a fixed horizontal base (x1, yCoord) to (x2, yCoord):
// We need to find a point (x3, y3) such that y3 != yCoord and |y3 - yCoord| is maximized.
// This means we need to find the global min_y and global max_y among all points.
// Let global_min_y = min(p[1] for p in coords) and global_max_y = max(p[1] for p in coords).
// The maximum possible height for any horizontal base at yCoord is max(yCoord - global_min_y, global_max_y - yCoord).
// This is true if there exists *any* point (x_any, global_min_y) or (x_any, global_max_y) such that x_any != x1 and x_any != x2.
// This is almost always true unless all points are on the same horizontal line.
// If all points are on the same horizontal line, no triangle with non-zero area can be formed.
// If there are points not on yCoord, then we can always pick one.

// Optimized approach for height:
// Precompute global min/max x and y coordinates.
// global_min_x, global_max_x, global_min_y, global_max_y.
// For a horizontal base (x1, yCoord) to (x2, yCoord):
//   Height candidate 1: yCoord - global_min_y (if global_min_y != yCoord)
//   Height candidate 2: global_max_y - yCoord (if global_max_y != yCoord)
//   The actual height is max of these, provided the point (x_any, global_min_y) or (x_any, global_max_y) exists and its x_any is not x1 or x2.
//   This check is tricky. If global_min_y is only available at x1 or x2, we can't use it.

// The problem statement "at least one side of this triangle is parallel to the x-axis or y-axis"
// Case 1: Base is parallel to x-axis. Points are (x1, y), (x2, y), (x3, y3).
//   Side ((x1,y), (x2,y)) is parallel to x-axis. Base = |x2-x1|. Height = |y3-y|.
//   To maximize, we need to maximize |x2-x1| and |y3-y|.
//   For each y-coordinate, find min_x and max_x. This gives max base for that y.
//   Then, for this y, find max |y3-y| over all other y3.
//   This means for each y, we need to know the min and max y-coordinates available globally.
//   Let min_y_overall = min(p[1]), max_y_overall = max(p[1]) from all points.
//   For a horizontal line at y_i, the max base is (max_x_on_y_i - min_x_on_y_i).
//   The max height for this y_i is max(y_i - min_y_overall, max_y_overall - y_i).
//   This is valid as long as min_y_overall != y_i and max_y_overall != y_i.
//   If min_y_overall == y_i, then we need to find the next smallest y.
//   If max_y_overall == y_i, then we need to find the next largest y.
//   This can be done by finding the overall min/max y from the `yMap` keys.
//   Let `overall_min_y_key = yMap.firstKey()` and `overall_max_y_key = yMap.lastKey()`.
//   And similarly for x.

// Revised logic for finding maxDoubleArea:
// 1. Pre-process coords into xMap and yMap (as done).
// 2. Calculate overall min/max x and y coordinates from the maps.
//    `overall_min_x = xMap.firstKey()`, `overall_max_x = xMap.lastKey()`
//    `overall_min_y = yMap.firstKey()`, `overall_max_y = yMap.lastKey()`

// 3. Iterate through `yMap` for horizontal bases:
//    For each `yCoord` and its `xCoords` (TreeMap of x-values on this line):
//      If `xCoords.size() < 2`, continue.
//      `base = xCoords.lastKey() - xCoords.firstKey()`. This is the maximum possible base on this line.
//      If `base == 0`, continue.
//      `height_candidate_1 = 0`, `height_candidate_2 = 0`.
//      If `yCoord != overall_min_y`: `height_candidate_1 = yCoord - overall_min_y`.
//      If `yCoord != overall_max_y`: `height_candidate_2 = overall_max_y - yCoord`.
//      `max_height_for_y = Math.max(height_candidate_1, height_candidate_2)`.
//      If `max_height_for_y > 0`: `maxDoubleArea = Math.max(maxDoubleArea, base * max_height_for_y)`.

// 4. Iterate through `xMap` for vertical bases:
//    For each `xCoord` and its `yCoords` (TreeMap of y-values on this line):
//      If `yCoords.size() < 2`, continue.
//      `base = yCoords.lastKey() - yCoords.firstKey()`.
//      If `base == 0`, continue.
//      `height_candidate_1 = 0`, `height_candidate_2 = 0`.
//      If `xCoord != overall_min_x`: `height_candidate_1 = xCoord - overall_min_x`.
//      If `xCoord != overall_max_x`: `height_candidate_2 = overall_max_x - xCoord`.
//      `max_height_for_x = Math.max(height_candidate_1, height_candidate_2)`.
//      If `max_height_for_x > 0`: `maxDoubleArea = Math.max(maxDoubleArea, base * max_height_for_x)`.

// This optimized approach:
// Building maps: O(N log N)
// Finding overall min/max: O(1) after maps are built.
// Iterating yMap: O(N_y) iterations. Inside, O(log N) for first/last key. Total O(N_y log N).
// Iterating xMap: O(N_x) iterations. Inside, O(log N) for first/last key. Total O(N_x log N).
// Overall Time Complexity: O(N log N).
// Space Complexity: O(N) for storing maps.

// Let's implement the optimized version.

class Solution {
    public long maxArea(int[][] coords) {
        // Store points grouped by x-coordinate and y-coordinate
        // Using TreeMap to keep coordinates sorted, which simplifies finding min/max
        java.util.Map<Integer, java.util.TreeMap<Integer, Integer>> xMap = new java.util.TreeMap<>(); // x -> {y -> count}
        java.util.Map<Integer, java.util.TreeMap<Integer, Integer>> yMap = new java.util.TreeMap<>(); // y -> {x -> count}

        for (int[] p : coords) {
            int x = p[0];
            int y = p[1];
            xMap.computeIfAbsent(x, k -> new java.util.TreeMap<>()).put(y, 1);
            yMap.computeIfAbsent(y, k -> new java.util.TreeMap<>()).put(x, 1);
        }

        long maxDoubleArea = -1;

        // If there are less than 3 points, no triangle can be formed.
        if (coords.length < 3) {
            return -1;
        }

        // Get overall min/max x and y coordinates
        int overallMinX = xMap.firstKey();
        int overallMaxX = xMap.lastKey();
        int overallMinY = yMap.firstKey();
        int overallMaxY = yMap.lastKey();

        // Case 1: Base parallel to x-axis (same y-coordinate)
        for (java.util.Map.Entry<Integer, java.util.TreeMap<Integer, Integer>> entryY : yMap.entrySet()) {
            int yCoord = entryY.getKey();
            java.util.TreeMap<Integer, Integer> xCoordsOnLine = entryY.getValue();

            if (xCoordsOnLine.size() < 2) { // Need at least two points for a base
                continue;
            }

            long base = (long)xCoordsOnLine.lastKey() - xCoordsOnLine.firstKey();
            if (base == 0) continue; // Base cannot be zero

            long maxHeight = 0;
            // Height candidate 1: distance to overallMinY
            if (yCoord != overallMinY) {
                maxHeight = Math.max(maxHeight, (long)yCoord - overallMinY);
            }
            // Height candidate 2: distance to overallMaxY
            if (yCoord != overallMaxY) {
                maxHeight = Math.max(maxHeight, (long)overallMaxY - yCoord);
            }

            if (maxHeight > 0) {
                maxDoubleArea = Math.max(maxDoubleArea, base * maxHeight);
            }
        }

        // Case 2: Base parallel to y-axis (same x-coordinate)
        for (java.util.Map.Entry<Integer, java.util.TreeMap<Integer, Integer>> entryX : xMap.entrySet()) {
            int xCoord = entryX.getKey();
            java.util.TreeMap<Integer, Integer> yCoordsOnLine = entryX.getValue();

            if (yCoordsOnLine.size() < 2) { // Need at least two points for a base
                continue;
            }

            long base = (long)yCoordsOnLine.lastKey() - yCoordsOnLine.firstKey();
            if (base == 0) continue; // Base cannot be zero

            long maxHeight = 0;
            // Height candidate 1: distance to overallMinX
            if (xCoord != overallMinX) {
                maxHeight = Math.max(maxHeight, (long)xCoord - overallMinX);
            }
            // Height candidate 2: distance to overallMaxX
            if (xCoord != overallMaxX) {
                maxHeight = Math.max(maxHeight, (long)overallMaxX - xCoord);
            }

            if (maxHeight > 0) {
                maxDoubleArea = Math.max(maxDoubleArea, base * maxHeight);
            }
        }

        return maxDoubleArea;
    }
}

// Time Complexity: O(N log N)
// Space Complexity: O(N)