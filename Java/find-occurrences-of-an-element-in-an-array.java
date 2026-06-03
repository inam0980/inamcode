class Solution {
    public int[] occurrencesOfElement(int[] nums, int[] queries, int x) {
        // Store indices of all occurrences of x
        java.util.List<Integer> occurrences = new java.util.ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == x) {
                occurrences.add(i);
            }
        }

        // Prepare the result array
        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int k = queries[i]; // k-th occurrence
            // Check if k-th occurrence exists (0-indexed list, so k-1)
            if (k > 0 && k <= occurrences.size()) {
                answer[i] = occurrences.get(k - 1);
            } else {
                answer[i] = -1; // Not enough occurrences or invalid k
            }
        }

        return answer;
    }
}
// Time complexity: O(N + Q) where N is the length of nums and Q is the length of queries.
// Space complexity: O(M) where M is the number of occurrences of x in nums.