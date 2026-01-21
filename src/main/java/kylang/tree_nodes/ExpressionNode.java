// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/** Abstract base for all expression nodes.
 * Serves as the root type of the parse tree. */
public abstract class ExpressionNode { public abstract int evaluate(Memory memory); }
