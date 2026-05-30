import java.util.*;

class Solution {
    public List<Boolean> getResults(int[][] queries) {
        // Use a TreeSet to store obstacle positions, ensuring sorted order and efficient lookups
        TreeSet<Integer> obstacles = new TreeSet<>();
        // Add a virtual obstacle at 0 to simplify calculations for the first segment
        obstacles.add(0);

        // Use a TreeMap to store available segment lengths and their counts.
        // Key: segment length, Value: count of segments with that length.
        // This allows efficient retrieval of the maximum available segment length.
        TreeMap<Integer, Integer> segmentLengths = new TreeMap<>();
        // Initially, there's an infinite segment from 0 to infinity.
        // We represent this with a very large number for practical purposes.
        // The problem constraints state x <= 5 * 10^4, so 10^9 is sufficiently large.
        segmentLengths.put(1_000_000_000, 1); 

        List<Boolean> results = new ArrayList<>();

        for (int[] query : queries) {
            if (query[0] == 1) { // Type 1 query: Add an obstacle
                int x = query[1];

                // Find the obstacle immediately before x and immediately after x
                Integer prevObstacle = obstacles.floor(x);
                Integer nextObstacle = obstacles.ceiling(x);

                // If nextObstacle is null, it means x is the largest obstacle so far.
                // In this case, the segment to its right is still infinite.
                // The segment to its left is from prevObstacle to x.
                if (nextObstacle == null) {
                    // Remove the old segment (prevObstacle to infinity)
                    int oldLength = 1_000_000_000;
                    segmentLengths.put(oldLength, segmentLengths.get(oldLength) - 1);
                    if (segmentLengths.get(oldLength) == 0) {
                        segmentLengths.remove(oldLength);
                    }
                    
                    // Add the new segment (prevObstacle to x)
                    int newLength = x - prevObstacle;
                    segmentLengths.put(newLength, segmentLengths.getOrDefault(newLength, 0) + 1);
                    
                    // Add the new infinite segment (x to infinity)
                    segmentLengths.put(1_000_000_000, segmentLengths.getOrDefault(1_000_000_000, 0) + 1);
                } else {
                    // x is between prevObstacle and nextObstacle.
                    // The segment (prevObstacle, nextObstacle) is split into two.
                    int oldLength = nextObstacle - prevObstacle;
                    
                    // Remove the old segment
                    segmentLengths.put(oldLength, segmentLengths.get(oldLength) - 1);
                    if (segmentLengths.get(oldLength) == 0) {
                        segmentLengths.remove(oldLength);
                    }

                    // Add the two new segments
                    int length1 = x - prevObstacle;
                    int length2 = nextObstacle - x;
                    segmentLengths.put(length1, segmentLengths.getOrDefault(length1, 0) + 1);
                    segmentLengths.put(length2, segmentLengths.getOrDefault(length2, 0) + 1);
                }
                
                // Add the new obstacle to the set
                obstacles.add(x);

            } else { // Type 2 query: Check if a block can be placed
                int x = query[1];
                int sz = query[2];

                // Find the obstacle immediately before or at x
                Integer prevObstacle = obstacles.floor(x);
                
                // The maximum available space ending at or before x is x - prevObstacle.
                // If this space is less than sz, we cannot place the block in this segment.
                if (x - prevObstacle < sz) {
                    results.add(false);
                    continue;
                }

                // Check if there's any segment with length >= sz.
                // The largest segment length is the last key in segmentLengths.
                // If the largest segment length is less than sz, it's impossible.
                if (segmentLengths.isEmpty() || segmentLengths.lastKey() < sz) {
                    results.add(false);
                    continue;
                }
                
                // If we reach here, it means:
                // 1. There's enough space in the segment ending at x (x - prevObstacle >= sz).
                // 2. There exists at least one segment somewhere with length >= sz.
                // This implies we can place the block.
                results.add(true);
            }
        }

        return results;
    }
}

// Time Complexity: O(Q log O) where Q is the number of queries and O is the number of obstacles.
// Each TreeSet/TreeMap operation (add, remove, floor, ceiling, lastKey) takes O(log O) time.
// In the worst case, O can be up to Q. So, O(Q log Q).
// Space Complexity: O(O) for storing obstacles and segment lengths. In the worst case, O(Q).