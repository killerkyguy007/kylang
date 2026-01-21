// Kyran Day, 12/07/2025
package kylang.statements;
import kylang.tree_nodes.BooleanExpressionNode;
import kylang.memory.Memory;
import kylang.tree_nodes.StatementList;

/**
 * Represents an if statement with optional elif and else clauses.
 * Grammar: if <boolean_expression> : <EOL> <statement_block> <remaining_if>
 * <remaining_if> -> <else_clause> | <elif_clause> <remaining_if> | null
 */
public class IfStatement extends Statement {
    
    private BooleanExpressionNode condition;
    private StatementList thenBlock;
    private IfStatement elifChain; // chain of elif statements or null
    private StatementList elseBlock; // else block or null
    
    public IfStatement(BooleanExpressionNode condition, StatementList thenBlock, 
                       IfStatement elifChain, StatementList elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elifChain = elifChain;
        this.elseBlock = elseBlock;
    }
    
    /** Execute this conditional statement using the provided memory.
     *
     * If the condition evaluates to true, executes the then-block. Otherwise,
     * if an elif chain exists it delegates execution to that chain. If neither
     * the condition nor any elif matches and an else block is present, executes
     * the else-block.
     *
     * @param memory the execution memory/state used to evaluate the condition and run blocks */
    @Override
    public void execute(Memory memory) {
        if (condition.evaluate(memory)) thenBlock.execute(memory);
        else if (elifChain != null) elifChain.execute(memory);
        else if (elseBlock != null) elseBlock.execute(memory);
    }
}
