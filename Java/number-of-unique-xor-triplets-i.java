class Solution {
    public int uniqueXorTriplets(int[] nums) {
        // Use a HashSet to store unique XOR triplet values
        java.util.HashSet<Integer> uniqueXorValues = new java.util.HashSet<>();
        int n = nums.length;

        // Iterate through all possible combinations of i, j, k such that i <= j <= k
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                for (int k = j; k < n; k++) {
                    // Calculate the XOR triplet value
                    int xorValue = nums[i] ^ nums[j] ^ nums[k];
                    // Add the value to the set
                    uniqueXorValues.add(xorValue);
                }
            }
        }

        // The size of the set is the number of unique XOR triplet values
        return uniqueXorValues.size();
    }
}
// Time complexity: O(N^3) due to three nested loops iterating up to N times.
// Space complexity: O(K) where K is the number of unique XOR values. In the worst case, K can be up to N^3, but practically it's bounded by the maximum possible XOR value (e.g., 2*N-1 for small N, or 2^B where B is max bits). For N=10^5, max XOR value can be around 2*10^5, so K is roughly O(N).