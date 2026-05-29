class Solution {
    int[][] grid;
    int n;
    // Stores island ID -> size. Island IDs start from 2.
    Map<Integer, Integer> islandSizes; 
    // Counter for assigning unique IDs to islands.
    int islandIdCounter; 

    // Directions for 4-directional connectivity (up, down, left, right)