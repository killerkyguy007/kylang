// Kyran Day, 12/07/2025
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Factor representing an identifier (variable reference).
 * Grammar: Factor ::= Id */
public final class IdentifierFactorNode extends FactorNode {
    final Token identifier;  // terminal: IDENTIFIER

    public IdentifierFactorNode(Token identifier) {
        this.identifier = identifier;
    }

    @Override
    int evaluate(Memory memory) {
        return memory.get(identifier.getLEXEME());
    }
}
