package kylang.statements;
// Kyran Day, 12/07/2025.
import kylang.tree_nodes.ExpressionNode;
import kylang.memory.Memory;
import kylang.tree_nodes.StatementList;

/**
 * Represents a for loop statement.
 * Grammar: for <id> in <arithmetic_expression> .. <arithmetic_expression> : <EOL> <statement_block>  */
public class ForStatement extends Statement {
    
    private String loopVariable;
    private ExpressionNode startExpr;
    private ExpressionNode endExpr;
    private StatementList body;
    
    public ForStatement(String loopVariable, ExpressionNode startExpr, ExpressionNode endExpr, StatementList body) {
        this.loopVariable = loopVariable;
        this.startExpr = startExpr;
        this.endExpr = endExpr;
        this.body = body;
    }
    
    /**
     * Executes the for-loop:
     * Evaluates the start and end expressions, then iterates from start to end (inclusive).
     * On each iteration the loop variable is bound in the provided memory and the loop body is executed.
     *
     * @param memory the execution memory/state used to evaluate expressions and store the loop variable
     */
    @Override
    public void execute(Memory memory) {
        int start = startExpr.evaluate(memory);
        int end = endExpr.evaluate(memory);
        
        for (int i = start; i <= end; i++) {
            memory.put(loopVariable, i);
            body.execute(memory);
        }
    }
}
