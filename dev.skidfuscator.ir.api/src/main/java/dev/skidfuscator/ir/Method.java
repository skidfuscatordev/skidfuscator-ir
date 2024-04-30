package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.impl.MethodModifier;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.method.AcknowledgeUnlinkGroupException;
import dev.skidfuscator.ir.method.MethodGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public non-sealed interface Method extends DirectMethodModifier {
    void visit(Klass owner, String name, MethodModifier modifier, List<Klass> args, Klass returnType);

    void visitGroup(MethodGroup group);

    InstructionsVisitor visitCode();

    /**
     * @return the parent Klass which owns this method
     */
    @NotNull Klass getOwner();

    /**
     * Sets the parent klass which owns the method.
     *
     * @param owner Parent klass wrapper
     * @throws IllegalStateException if the owner is null
     */
    void setOwner(@NotNull Klass owner);

    /**
     * @return The method group it belongs to, null
     * if the method has been manually removed
     * from the group. If static, the method
     * group will be unique.
     */
    @Nullable MethodGroup getGroup();

    /**
     * @param group Sets the group of the method. This
     *              is an unsafe method and should only
     *              really be accessed by MethodGroup
     *              or if you really know what you're
     *              doing.
     * @see MethodGroup#addMethod(JavaMethod)
     * @see MethodGroup#removeMethod(JavaMethod)
     */
    void setGroup(@Nullable MethodGroup group);

    /**
     * @return The name of the method
     */
    @NotNull String getName();

    /**
     * Sets name the name of the method. You should be
     * using MethodGroup#setName in most cases. Changing
     * the name will force the parent group to unlink. If
     * there is a parent group, the AcknowledgeUnlinkGroupException
     * exception will be thrown.
     *
     * @param name the new name of the method
     * @throws IllegalStateException           if the name is null
     * @throws AcknowledgeUnlinkGroupException If there is a
     *                                         parent group, the exception will be thrown to
     *                                         force the developer to acknowledge this implicit
     *                                         change.
     * @see MethodGroup#setArgs(List)
     */
    void setName(@NotNull String name) throws AcknowledgeUnlinkGroupException;

    /**
     * @return an unmodifiable collection of the arguments
     */
    @NotNull List<Klass> getArgs();

    /**
     * Sets the method arguments for this method. You should
     * be using MethodGroup#setArgs in most cases. Changing
     * the name will force the parent group to unlink. If
     * there is a parent group, the AcknowledgeUnlinkGroupException
     * exception will be thrown.
     *
     * @param args The method arguments for the method
     * @throws IllegalStateException           if the arguments are null
     * @throws AcknowledgeUnlinkGroupException If there is a
     *                                         parent group, the exception will be thrown to
     *                                         force the developer to acknowledge this implicit
     *                                         change.
     * @see MethodGroup#setArgs(List)
     */
    void setArgs(@NotNull List<Klass> args) throws AcknowledgeUnlinkGroupException;

    /**
     * @return The return type of the method
     */
    @NotNull Klass getReturnType();

    /**
     * Sets return type.
     *
     * @param returnType the return type
     * @throws AcknowledgeUnlinkGroupException the acknowledge unlink group exception
     */
    void setReturnType(@NotNull Klass returnType) throws AcknowledgeUnlinkGroupException;

    /**
     * @return whether the method is an object initializer
     */
    boolean isInit();

    boolean isClassInit();
}
