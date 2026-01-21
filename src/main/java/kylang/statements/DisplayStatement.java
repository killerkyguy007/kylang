// Kyran Day, 12/07/2025
package kylang.statements;
import kylang.memory.Memory;

/**
 * The DisplayStatement class represents a statement that displays
 * the value of a specified variable stored in memory.
 *
 * This class extends the abstract Statement class and provides an
 * implementation for the execute method. When executed, it fetches
 * the value of a variable from a shared memory space and displays it.
 */
public class DisplayStatement extends Statement {

    private String id;

    public DisplayStatement(String id) {
        this.id = id;
    }

    public void execute(Memory memory) {
        System.out.println(memory.get(id));
    }
}
