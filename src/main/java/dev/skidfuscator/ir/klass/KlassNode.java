package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;
import java.util.List;

/**
 * Wrapper for ASM class nodes with extended support such
 * as proper linking to super-class, interfaces, better
 * methods to quick access info such as static or interface
 * access etc.
 */
public interface KlassNode {
    boolean isResolvedHierarchy();

    boolean isResolvedInternal();

    void resolveHierarchy();

    void lock();

    /**
     * Resolve the KlassNode. This should be invoked in a BFS
     * fashion from top-down hierarchy tree. The first Klass
     * to be resolved henceforth should be the java/lang/Object
     * class.
     */
    void resolveInternal();

    /**
     * Resolve the instructions of the methods of the KlassNode.
     */
    void resolveInstructions();

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
     * Retrieves the signature of the class.
     *
     * @return The signature of the class
     */
    String getSignature();


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
    Collection<FunctionNode> getMethods();

    /**
     * Gets method by name and descriptor.
     *
     * @param name the name
     * @param desc the desc
     * @return the method
     */
    FunctionNode getMethod(String name, String desc);

    /**
     * Gets method by name and descriptor.
     * @param nameAndDesc the descriptor
     * @return the method
     */
    default FunctionNode getMethod(String nameAndDesc) {
        final String[] splits = nameAndDesc.split("\\(");
        return getMethod(splits[0], "(" + splits[1]);
    }

    /**
     * Gets field by name and descriptor.
     * @param name the name
     * @param desc the desc
     * @return the method
     */
    FieldNode getField(String name, String desc);

    /**
     * Gets field by name and descriptor.
     * @param nameAndDesc the descriptor
     * @return the method
     */
    default FieldNode getField(String nameAndDesc) {
        final String[] splits = nameAndDesc.split("\\(");
        return getField(splits[0], "(" + splits[1]);
    }

    /**
     * Add method.
     *
     * @param node the node
     */
    void addMethod(final FunctionNode node);

    /**
     * Remove method.
     *
     * @param node the node
     */
    void removeMethod(final FunctionNode node);

    /**
     * @return  List of wrapped fields
     */
    @NotNull
    Collection<FieldNode> getFields();

    /**
     * Adds a field
     */
    void addField(final FieldNode node);

    /**
     * Removes a field.
     * @param node the node
     */
    void removeField(final FieldNode node);

    /**
     * Is interface boolean.
     *
     * @return the boolean
     */
    boolean isInterface();

    /**
     * Dumps the wrapper back to the original file.
     */
    ClassNode dump();

}
