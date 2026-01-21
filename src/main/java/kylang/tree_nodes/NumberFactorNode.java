// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/** Factor representing a numeric literal.
 * Grammar: Number ::= INT_LIT */
public final class NumberFactorNode extends FactorNode {
    final Token intLit;          // terminal: INT_LIT

    public NumberFactorNode(Token intLit) {
        this.intLit = intLit;
    }

    @Override
    public int evaluate(Memory memory) { return Integer.parseInt(intLit.getLEXEME()); }
}
