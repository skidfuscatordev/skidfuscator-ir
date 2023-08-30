package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.TryCatchBlock;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.util.Descriptor;
import dev.skidfuscator.ir.variable.LocalVariable;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;

public interface FunctionNode extends HierarchyResolvable {

    void lock();

    boolean isLocked();

    Descriptor getOriginalDescriptor();

    boolean isSynthetic();

    boolean isResolved();

    Set<FunctionNode> getParents();

    void addParent(final FunctionNode functionNode);

    void removeParent(final FunctionNode functionNode);

    boolean hasParent(final FunctionNode functionNode);

    Set<FunctionNode> getChildren();

    void addChild(final FunctionNode functionNode);

    void removeChild(final FunctionNode functionNode);

    boolean hasChild(final FunctionNode functionNode);

    /**
     * Resolves the function, parsing through
     * instructions and resolving method groups.
     */
    void resolveHierarchy();

    /**
     * Resolves the instructions links of the
     * function and internal operations.
     *
     * THIS IS OPTIONAL IF YOU WISH TO PERFORM
     * ANALYSIS ON THIS FUNCTION
     */
    void resolveInternal();

    /**
     * Method used to dump the information from
     * the function node. Currently not implemented.
     */
    MethodNode dump();

    /**
     * Retrieves the instructions of the function.
     *
     * @return The list of instructions
     */
    InstructionList getInstructions();

    /**
     * Retrieves the try catch blocks of the function.
     *
     * @return The list of try catch blocks
     */
    List<TryCatchBlock> getTryCatchBlocks();

    /**
     * Retrieves the local variables of the function.
     *
     * @return The list of local variables
     */
    List<LocalVariable> getLocalVariables();

    /**
     * Resolves all the invocations of the function.
     *
     * @return The list of invocations
     */
    List<FunctionInvoker<?, ?>> getInvokes();

    /**
     * Adds an invocation to the function.
     *
     * @param invoker The invoker to add
     */
    void addInvoke(final FunctionInvoker<?, ?> invoker);

    /**
     * Removes an invocation from the function.
     * @param invoker The invoker to remove
     */
    void removeInvoke(final FunctionInvoker<?, ?> invoker);

    /**
     * Retrieves the exceptions of the function.
     *
     * @return The list of exceptions
     */
    List<KlassNode> getExceptions();

    /**
     * Retrieves the parent class of the function.
     *
     * @return The parent class of the function
     */
    KlassNode getOwner();

    /**
     * Sets the parent class of the function.
     *
     * @param node The parent class to set
     */
    void setOwner(final KlassNode node);

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
     * Returns if the function can be modified (name, desc, etc)
     * safely without conflict.
     *
     * @return  boolean returning whether the function can be
     *          modified without consequences
     */
    boolean isMutable();

    /**
     * Determines if the function is static.
     *
     * @return True if the function is static, false otherwise
     */
    boolean isStatic();

    /**
     * Determines if the function is private.
     *
     * @return True if the function is private,
     */
    boolean isPrivate();

    /**
     * Determines if the function is a constructor.
     *
     * @return  True if the function is a constructor,
     *          false otherwise
     */
    boolean isConstructor();

    /**
     * Determines if the function is a class init.
     *
     * @return  True if the function is a class init,
     *          false otherwise
     */
    boolean isClassInit();
}
