/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode createBinaryTree(int[][] descriptions) {
        // Map to store nodes by their value for quick access
        Map<Integer, TreeNode> nodeMap = new HashMap<>();
        // Set to keep track of all child nodes
        Set<Integer> children = new HashSet<>();

        // First pass: create all nodes and establish parent-child relationships
        for (int[] desc : descriptions) {
            int parentVal = desc[0];
            int childVal = desc[1];
            int isLeft = desc[2];

            // Get or create parent node
            nodeMap.putIfAbsent(parentVal, new TreeNode(parentVal));
            TreeNode parentNode = nodeMap.get(parentVal);

            // Get or create child node
            nodeMap.putIfAbsent(childVal, new TreeNode(childVal));
            TreeNode childNode = nodeMap.get(childVal);

            // Assign child to parent's left or right
            if (isLeft == 1) {
                parentNode.left = childNode;
            } else {
                parentNode.right = childNode;
            }

            // Mark childVal as a child
            children.add(childVal);
        }

        // Second pass: find the root node
        // The root is the only node that is not a child of any other node
        for (int[] desc : descriptions) {
            int parentVal = desc[0];
            if (!children.contains(parentVal)) {
                return nodeMap.get(parentVal);
            }
        }

        return null; // Should not reach here given problem constraints
    }
}
// Time complexity: O(N), where N is the number of descriptions. We iterate through descriptions twice.
// Space complexity: O(N), for storing nodes in the map and child values in the set.