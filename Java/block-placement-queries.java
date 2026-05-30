import java.util.*;

class Solution {
    public List<Boolean> getResults(int[][] queries) {
        // Use a TreeSet to store obstacle positions, automatically sorted
        TreeSet<Integer> obstacles = new TreeSet<>();
        // Add a virtual obstacle at 0 to simplify calculations for the first segment
        obstacles.add(0);

        // Use a TreeMap to store segments by their start positions.
        // Key: start of segment, Value: end of segment
        TreeMap<Integer, Integer> segments = new TreeMap<>();
        // Initially, there's one large segment from 0 to infinity (represented by a large number)
        // For practical purposes, we can use a value larger than any possible x.
        // Max x is 5*10^4, so 5*10^4 + 1 is safe.
        segments.put(0, 50001); 

        // Use a TreeMap to store available gaps (max block sizes) by their size.
        // Key: gap size, Value: count of gaps with this size
        TreeMap<Integer, Integer> gaps = new TreeMap<>();
        // Initially, the gap from 0 to 50001 has size 50001.
        gaps.put(50001, 1);

        List<Boolean> results = new ArrayList<>();

        for (int[] query : queries) {
            if (query[0] == 1) { // Type 1 query: Add obstacle
                int x = query[1];

                // Find the segment that contains x
                Map.Entry<Integer, Integer> floorEntry = segments.floorEntry(x);
                int segStart = floorEntry.getKey();
                int segEnd = floorEntry.getValue();

                // Remove the old segment's gap size from 'gaps'
                int oldGapSize = segEnd - segStart;
                gaps.put(oldGapSize, gaps.get(oldGapSize) - 1);
                if (gaps.get(oldGapSize) == 0) {
                    gaps.remove(oldGapSize);
                }

                // Remove the old segment from 'segments'
                segments.remove(segStart);

                // Add new segments if they exist
                if (x > segStart) { // Segment [segStart, x)
                    segments.put(segStart, x);
                    gaps.put(x - segStart, gaps.getOrDefault(x - segStart, 0) + 1);
                }
                if (x < segEnd) { // Segment (x, segEnd]
                    segments.put(x, segEnd);
                    gaps.put(segEnd - x, gaps.getOrDefault(segEnd - x, 0) + 1);
                }

                // Add the new obstacle
                obstacles.add(x);

            } else { // Type 2 query: Check block placement
                int x = query[1];
                int sz = query[2];

                // Find the largest obstacle at or before x
                Integer prevObstacle = obstacles.floor(x);
                // The available space for a block ending at x is [prevObstacle, x]
                int availableSpace = x - prevObstacle;

                // Check if a block of size sz can fit in this direct space
                boolean canFitDirectly = (availableSpace >= sz);

                // Check if a block of size sz can fit in any other gap
                // We need to find if there's any gap with size >= sz
                // gaps.ceilingEntry(sz) finds the smallest gap size >= sz
                boolean canFitInAnyGap = (gaps.ceilingEntry(sz) != null);

                results.add(canFitDirectly || canFitInAnyGap);
            }
        }

        return results;
    }
}

// Time Complexity: O(Q log N) where Q is the number of queries and N is the number of obstacles.
// Each query involves TreeSet/TreeMap operations (add, remove, floor, ceiling) which take O(log N) time.
// N can be up to Q. So, O(Q log Q).
// Space Complexity: O(N) for storing obstacles, segments, and gaps. N can be up to Q. So, O(Q).