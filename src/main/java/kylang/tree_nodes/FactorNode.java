// Kyran Day, 12/07/2025
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Abstract base for all factor nodes.
 * Factors are the leaves of the expression hierarchy.
 */
public abstract class FactorNode { abstract int evaluate(Memory memory); }
