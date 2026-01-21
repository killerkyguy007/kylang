// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Represents a single factor term.
 * Grammar: Term ::= Factor
 */
public final class UnaryTermNode extends TermNode {
    final FactorNode factor; // child non-terminal

    public UnaryTermNode(FactorNode factor) {
        this.factor = factor;
    }

    @Override
    public int evaluate(Memory memory) {
        return factor.evaluate(memory);
    }
}
