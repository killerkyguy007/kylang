// Kyran Day, 12/07/2025
package kylang.statements;
import kylang.memory.Memory;
/**
 * The Statement class serves as a blueprint for creating executable statements.
 * It is an abstract class that defines the structure for specific types of statements
 * that need to implement the execute method.
 */
public abstract class Statement {

    Statement() {}

    public abstract void execute(Memory memory);
}
