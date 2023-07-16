package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * Wrapper for ASM class nodes with extended support such
 * as proper linking to super-class, interfaces, better
 * methods to quick access info such as static or interface
 * access etc.
 */
public interface KlassNode {
    /**
     * Resolve the KlassNode. This should be invoked in a BFS
     * fashion from top-down hierarchy tree. The first Klass
     * to be resolved henceforth should be the java/lang/Object
     * class.
     */
    void resolve();

    /**
     * @return  Returns self as a Type
     */
    @NotNull
    Type asType();

    /**
     * @return  Name of the class with the following format:
     *          my/first/Class
     */
    @NotNull
    String getName();

    /**
     * Sets the name of the class, which is exported at dump.
     * Since the KlassNode is referenced by its callers, it
     * *also* updates their respective calls.
     *
     * @param name new name for the class
     */
    void setName(@NotNull final String name);

    /**
     * @return  Gets the super class wrapper
     */
    @Nullable
    KlassNode getParent();

    /**
     * Sets the super class wrapper
     *
     * @param klass the superclass class
     */
    void setParent(@Nullable final KlassNode klass);

    /**
     * @return  gets an immutable list copy of the
     *          implemented interfaces by the wrapped
     *          class
     */
    @NotNull
    List<KlassNode> getInterfaces();

    /**
     * Sets the implementing interface by the wrapped
     * class
     *
     * @param klasses the collection of implementing
     *                interfaces
     */
    void setInterfaces(@Nullable final List<KlassNode> klasses);

    /**
     * @return  List of wrapped methods
     */
    @NotNull
    List<FunctionNode> getMethods();

    /**
     * Sets methods.
     *
     * @param nodes the nodes
     */
    void setMethods(@Nullable final List<FunctionNode> nodes);

    /**
     * Add method.
     *
     * @param node the node
     */
    void addMethod(final FunctionNode node);

    /**
     * Is interface boolean.
     *
     * @return the boolean
     */
    boolean isInterface();

    /**
     * Dumps the wrapper back to the original file.
     */
    void dump();
}
