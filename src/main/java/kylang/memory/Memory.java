// Kyran Day, 12/07/2025.
package kylang.memory;
import java.util.Hashtable;
/**
 * TreeNodes.Memory
 * ------------------------------------------------------------
 * Provides a shared memory space for variable storage.
 */
public class Memory {

    private final Hashtable<String, Integer> table = new Hashtable<>(); // Hash table mem, string as key to map to ints
    /**
     * Retrieves the integer value of a variable from memory.
     *
     * @param id the variable name
     * @return the stored integer value, or 0 if not found */
    public int get(String id) {
        Integer value = table.get(id.toLowerCase());
        return (value != null) ? value : 0;
    }

    /**
     * Stores a variable and its integer value in memory.
     *
     * @param id  the variable name
     * @param value the integer value to associate with the variable */
    public void put(String id, int value) {
        table.put(id.toLowerCase(), value);
    }
}
