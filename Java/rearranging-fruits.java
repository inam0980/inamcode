class Solution {
    public long minCost(int[] basket1, int[] basket2) {
        // Use a frequency map to count all fruits across both baskets
        // This helps determine the target state and identify mismatches
        java.util.TreeMap<Integer, Integer> counts = new java.util.TreeMap<>();
        for (int fruit : basket1) {
            counts.put(fruit, counts.getOrDefault(fruit, 0) + 1);
        }
        for (int fruit : basket2) {
            counts.put(fruit, counts.getOrDefault(fruit, 0) + 1);
        }

        // Check if it's possible to make baskets equal
        // Each fruit type must have an even total count across both baskets
        for (int count : counts.values()) {
            if (count % 2 != 0) {
                return -1; // Impossible if any fruit count is odd
            }
        }

        // Identify fruits that need to be moved from one basket to another
        // These are fruits that appear more often in one basket than they should
        java.util.ArrayList<Integer> toSwap = new java.util.ArrayList<>();
        for (int fruit : basket1) {
            counts.put(fruit, counts.get(fruit) - 1); // Decrement count for basket1
        }
        // Now, counts.get(fruit) represents (count_in_basket2 - count_in_basket1)
        // If counts.get(fruit) > 0, it means basket2 has more of this fruit than basket1
        // If counts.get(fruit) < 0, it means basket1 has more of this fruit than basket2
        // We only care about the "excess" fruits that need to be moved.
        // For example, if counts.get(fruit) is -2, basket1 has 2 more than it should.
        // If counts.get(fruit) is 2, basket2 has 2 more than it should.
        // In both cases, 2 fruits of this type need to be involved in swaps.
        // We add half of the absolute difference to `toSwap` list.
        // Since we iterate through the map in sorted order, `toSwap` will be sorted.
        for (java.util.Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            int fruit = entry.getKey();
            int diff = entry.getValue(); // This is count_in_basket2 - count_in_basket1
            
            // If diff > 0, basket2 has an excess of 'diff' fruits compared to basket1.
            // Half of these 'diff' fruits (diff / 2) need to be moved from basket2 to basket1.
            // If diff < 0, basket1 has an excess of '-diff' fruits compared to basket2.
            // Half of these '-diff' fruits (-diff / 2) need to be moved from basket1 to basket2.
            // In both cases, we add abs(diff)/2 fruits to the `toSwap` list.
            // The `toSwap` list will contain fruits that are "excess" in one basket
            // and need to be moved to the other.
            // For example, if diff is 4, it means 2 fruits of this type need to be moved from basket2.
            // If diff is -4, it means 2 fruits of this type need to be moved from basket1.
            // We add the fruit value to `toSwap` list `Math.abs(diff) / 2` times.
            for (int i = 0; i < Math.abs(diff) / 2; i++) {
                toSwap.add(fruit);
            }
        }

        long totalCost = 0;
        // The minimum cost for a swap is min(basket1[i], basket2[j]).
        // We want to use the smallest possible costs for swaps.
        // The smallest possible cost for any swap is the minimum fruit cost overall (minVal).
        // We can use minVal to swap any two fruits.
        // If we need to swap 'k' fruits, we pick the 'k' smallest fruits from `toSwap`
        // and swap them. The cost is the sum of these 'k' smallest fruits.
        // However, if we can use the global minimum fruit cost (minVal) for a swap,
        // and minVal is smaller than the fruit we are about to swap, it's always better
        // to swap with minVal.
        // The number of swaps needed is `toSwap.size() / 2`.
        // We iterate through the first half of `toSwap` (which are the smallest fruits
        // that need to be moved).
        // For each such fruit, the cost is `min(fruit_to_move, 2 * minVal)`.
        // Why `2 * minVal`? Because a swap operation costs `min(basket1[i], basket2[j])`.
        // If we swap fruit A from basket1 with fruit B from basket2, cost is min(A, B).
        // If we want to move fruit A from basket1 to basket2, and fruit B from basket2 to basket1,
        // we can swap A and B directly. Cost is min(A, B).
        // Alternatively, we can move A from basket1 to basket2 by swapping A with minVal from basket2.
        // Then move B from basket2 to basket1 by swapping B with minVal from basket1.
        // This is not quite right.
        // The problem states "swap the ith fruit of basket1 with the jth fruit of basket2".
        // This means we are moving one fruit from basket1 to basket2, and one from basket2 to basket1.
        // The `toSwap` list contains fruits that are "out of place".
        // For example, if `toSwap` is [1, 2, 3, 4], it means we have an excess of 1 and 2 in one basket,
        // and an excess of 3 and 4 in the other.
        // We need to perform `toSwap.size() / 2` swaps.
        // The most efficient swaps are between the smallest fruits in `toSwap`.
        // Let `minVal` be the smallest fruit cost overall (from `counts.firstKey()`).
        // For each fruit `f` in the first half of `toSwap` (smallest `f`s),
        // we can either swap `f` with another fruit `g` from the second half of `toSwap`
        // (cost `min(f, g)`), or we can use `minVal` as an intermediary.
        // If we swap `f` with `minVal` (cost `min(f, minVal)`) and then swap `minVal` with `g`
        // (cost `min(minVal, g)`), this is not a single operation.
        // A single operation is swapping `basket1[i]` with `basket2[j]`.
        // The key insight is that we need to move `toSwap.size() / 2` fruits.
        // We pick the `toSwap.size() / 2` smallest fruits from the `toSwap` list.
        // For each such fruit `f`, we need to move it.
        // The cost of moving `f` is `min(f, 2 * minVal)`.
        // Why `2 * minVal`?
        // Consider a fruit `f` that needs to be moved from basket A to basket B.
        // We can swap `f` with some `g` from basket B. Cost `min(f, g)`.
        // Or, we can swap `f` with `minVal` from basket B. Cost `min(f, minVal)`.
        // This `minVal` is now in basket A. We need to move it back to basket B.
        // We can swap this `minVal` (now in basket A) with another `minVal` (still in basket B).
        // This is effectively two swaps:
        // 1. Swap `f` (from A) with `minVal` (from B). Cost `min(f, minVal)`.
        //    Basket A now has `minVal`, Basket B has `f`.
        // 2. Swap `minVal` (from A) with `minVal` (from B). Cost `min(minVal, minVal) = minVal`.
        //    Basket A now has `minVal`, Basket B has `minVal`.
        // This effectively moves `f` from A to B, and `minVal` from B to A, and then `minVal` from A to B.
        // This is confusing.
        // The simpler explanation:
        // We have `k = toSwap.size() / 2` fruits that are "excess" in one basket and need to be moved.
        // These are the `k` smallest values in the `toSwap` list.
        // For each of these `k` fruits `f`, we need to pay a cost to move it.
        // The cost is `min(f, 2 * minVal)`.
        // `f`: direct swap with another "excess" fruit.
        // `2 * minVal`: two swaps using the global minimum fruit `minVal` as an intermediary.
        // Example: move `f` from basket1 to basket2.
        // 1. Swap `f` (basket1) with `minVal` (basket2). Cost `min(f, minVal)`.
        //    Now `minVal` is in basket1, `f` is in basket2.
        // 2. We need to move `minVal` back to basket2. Swap `minVal` (basket1) with `minVal` (basket2). Cost `minVal`.
        //    This effectively moves `f` from basket1 to basket2, and leaves the `minVal`s in their original baskets.
        //    Total cost for moving `f` this way is `min(f, minVal) + minVal`.
        //    Since `f >= minVal`, `min(f, minVal)` is `minVal`. So total cost is `minVal + minVal = 2 * minVal`.
        // Therefore, for each of the `k` smallest fruits in `toSwap`, the cost is `min(fruit_value, 2 * minVal)`.

        int minVal = counts.firstKey(); // Smallest fruit cost overall
        int numSwaps = toSwap.size() / 2; // Number of actual swaps needed

        for (int i = 0; i < numSwaps; i++) {
            totalCost += Math.min(toSwap.get(i), 2 * minVal);
        }

        return totalCost;
    }
}

// Time Complexity: O(N log N) due to TreeMap operations and sorting of `toSwap` (implicitly by adding to ArrayList from sorted TreeMap).
// Space Complexity: O(N) for the TreeMap and ArrayList.