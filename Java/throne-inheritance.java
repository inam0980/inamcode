import java.util.*;

class ThroneInheritance {
    // Map to store family tree: parentName -> list of children names
    private Map<String, List<String>> familyTree;
    // Set to store names of dead people for quick lookup
    private Set<String> deadPeople;
    // Name of the king
    private String kingName;

    public ThroneInheritance(String kingName) {
        this.kingName = kingName;
        this.familyTree = new HashMap<>();
        this.deadPeople = new HashSet<>();
        // Initialize king in the family tree, even if no children yet
        familyTree.put(kingName, new ArrayList<>());
    }
    
    public void birth(String parentName, String childName) {
        // Add child to parent's list of children
        familyTree.computeIfAbsent(parentName, k -> new ArrayList<>()).add(childName);
        // Ensure child also has an entry in the family tree for future children
        familyTree.putIfAbsent(childName, new ArrayList<>());
    }
    
    public void death(String name) {
        // Mark the person as dead
        deadPeople.add(name);
    }
    
    public List<String> getInheritanceOrder() {
        List<String> order = new ArrayList<>();
        // Perform a DFS traversal to get the inheritance order
        dfs(kingName, order);
        return order;
    }

    // Depth-First Search to traverse the family tree in inheritance order
    private void dfs(String currentPerson, List<String> order) {
        // If the current person is not dead, add them to the order
        if (!deadPeople.contains(currentPerson)) {
            order.add(currentPerson);
        }

        // Recursively call DFS for each child in their birth order
        // Children are stored in order of birth in the list
        for (String child : familyTree.getOrDefault(currentPerson, Collections.emptyList())) {
            dfs(child, order);
        }
    }
}

/**
 * Your ThroneInheritance object will be instantiated and called as such:
 * ThroneInheritance obj = new ThroneInheritance(kingName);
 * obj.birth(parentName,childName);
 * obj.death(name);
 * List<String> param_3 = obj.getInheritanceOrder();
 */

// Time Complexity:
// Constructor: O(1)
// birth: O(1) average, O(N) worst case for HashMap (resizing), but practically O(1)
// death: O(1) average for HashSet
// getInheritanceOrder: O(N + E) where N is the number of people (nodes) and E is the number of parent-child relationships (edges).
// This is because it performs a DFS traversal of the entire family tree. In a tree, E = N-1, so it's O(N).
//
// Space Complexity:
// O(N) where N is the total number of people ever born.
// familyTree: Stores N people and their children lists. Each person appears once as a key, and once as a value for their parent.
// deadPeople: Stores up to N dead people.
// getInheritanceOrder (recursive stack): O(H) where H is the maximum depth of the family tree (height of the tree). In worst case, H can be N.