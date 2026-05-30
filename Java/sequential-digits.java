import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Solution {
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> result = new ArrayList<>();

        // Iterate through all possible starting digits (1 to 9)
        for (int startDigit = 1; startDigit <= 9; startDigit++) {
            int currentNum = startDigit;
            int nextDigit = startDigit + 1;

            // Build sequential numbers starting with 'startDigit'
            while (currentNum <= high && nextDigit <= 9) {
                currentNum = currentNum * 10 + nextDigit; // Append the next digit
                
                // Check if the generated number is within the range
                if (currentNum >= low && currentNum <= high) {
                    result.add(currentNum);
                }
                nextDigit++; // Move to the next digit in the sequence
            }
        }
        
        // The problem statement implies a sorted list, and our generation method
        // naturally produces numbers in increasing order of length, then value.
        // However, to be absolutely sure and robust, a sort is good practice.
        Collections.sort(result); 
        
        return result;
    }
}

// Time Complexity: O(1) - The maximum number of sequential digits is 9 (123456789).
// There are at most 9 starting digits. For each starting digit, we generate at most 9 numbers.
// The total number of sequential digits is small and constant (around 36 numbers).
// Sorting a constant number of elements is also O(1).
// Space Complexity: O(1) - The list stores at most a constant number of sequential digits (max 36).