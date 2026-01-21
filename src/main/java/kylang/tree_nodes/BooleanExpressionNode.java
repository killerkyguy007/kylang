// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Abstract base for all boolean expression nodes.
 * Boolean expressions evaluate to a boolean value. */
public abstract class BooleanExpressionNode { public abstract boolean evaluate(Memory memory); }
