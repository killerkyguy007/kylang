// Kyran Day, 12/07/2025
package kylang.statements;
import kylang.tree_nodes.ExpressionNode;
import kylang.memory.Memory;

/**
 * Represents an assignment statement in the program.
 *
 * This class handles the execution of assignment operations, where a variable
 * is assigned the value of an evaluated expression. The assignment statement
 * evaluates an expression and stores the resulting integer value in memory
 * under the specified variable identifier. */
public class AssignmentStatement extends Statement {

    private String id;
    private ExpressionNode expression;

    /**
     * Constructs an AssignmentStatement with a variable identifier and expression.
     *
     * @param id the variable identifier to assign to
     * @param expression the expression node to evaluate */
    public AssignmentStatement(String id, ExpressionNode expression) {
        this.id = id;
        this.expression = expression;
    }

    /**
     * Executes the assignment statement by evaluating the expression and storing
     * the result in memory under the specified variable identifier.
     *
     * @param memory the memory object where the variable value will be stored */
    public void execute(Memory memory) {
        int value = expression.evaluate(memory);
        memory.put(id, value);
    }
}
