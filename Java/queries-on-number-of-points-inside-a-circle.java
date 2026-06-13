class Solution {
    public int[] countPoints(int[][] points, int[][] queries) {
        int numQueries = queries.length;
        int[] answer = new int[numQueries];

        // Iterate through each query
        for (int i = 0; i < numQueries; i++) {
            int centerX = queries[i][0];
            int centerY = queries[i][1];
            int radius = queries[i][2];
            int radiusSq = radius * radius; // Use squared radius for distance comparison

            int pointsInside = 0;
            // Iterate through each point
            for (int[] point : points) {
                int pointX = point[0];
                int pointY = point[1];

                // Calculate squared distance from point to circle center
                int distXSq = (pointX - centerX) * (pointX - centerX);
                int distYSq = (pointY - centerY) * (pointY - centerY);
                int distSq = distXSq + distYSq;

                // If squared distance is less than or equal to squared radius, point is inside
                if (distSq <= radiusSq) {
                    pointsInside++;
                }
            }
            answer[i] = pointsInside;
        }

        return answer;
    }
}
// Time complexity: O(Q * P) where Q is the number of queries and P is the number of points.
// Space complexity: O(Q) for storing the answer array.