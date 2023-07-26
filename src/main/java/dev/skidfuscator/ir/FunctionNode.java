package dev.skidfuscator.ir;

import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionGroup;
import dev.skidfuscator.ir.method.FunctionInvoker;

import java.util.List;

public interface FunctionNode extends HierarchyResolvable {

    /**
     * Resolves the function, parsing through
     * instructions and resolving method groups.
     */
    void resolve();

    /**
     * Method used to dump the information from
     * the function node. Currently not implemented.
     */
    void dump();

    /**
     * Retrieves the instructions of the function.
     *
     * @return The list of instructions
     */
    List<Insn> getInstructions();

    /**
     * Resolves all the invocations of the function.
     *
     * @return The list of invocations
     */
    List<FunctionInvoker<?>> getInvokes();

    /**
     * Adds an invocation to the function.
     *
     * @param invoker The invoker to add
     */
    void addInvoke(final FunctionInvoker<?> invoker);

    /**
     * Removes an invocation from the function.
     * @param invoker The invoker to remove
     */
    void removeInvoke(final FunctionInvoker<?> invoker);

    /**
     * Retrieves the parent class of the function.
     *
     * @return The parent class of the function
     */
    KlassNode getParent();

    /**
     * Sets the parent class of the function.
     *
     * @param node The parent class to set
     */
    void setParent(final KlassNode node);

    /**
     * Retrieves the group of the function.
     *
     * @return The group of the function
     */
    FunctionGroup getGroup();

    /**
     * Sets the group of the function, verifying no
     * conflicts with existing group.
     *
     * @param   group The group to set
     * @throws  IllegalStateException if there's a conflict
     *          with the current group
     */
    void setGroup(final FunctionGroup group);

    /**
     * Retrieves the name of the function.
     *
     * @return The name of the function
     */
    String getName();

    /**
     * Sets the name of the function.
     *
     * @param name The name to set
     */
    void setName(final String name);

    /**
     * Retrieves the descriptor of the function.
     *
     * @return The descriptor of the function
     */
    String getDesc();

    /**
     * Determines if the function is static.
     *
     * @return True if the function is static, false otherwise
     */
    boolean isStatic();

    /**
     * Determines if the function is a constructor.
     *
     * @return  True if the function is a constructor,
     *          false otherwise
     */
    boolean isConstructor();
}
