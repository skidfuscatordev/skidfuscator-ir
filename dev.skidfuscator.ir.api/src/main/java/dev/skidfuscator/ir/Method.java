package dev.skidfuscator.ir;

import dev.skidfuscator.ir.insn.impl.AbstractInstructionList;
import dev.skidfuscator.ir.insn.impl.AbstractInstructionsVisitor;
import dev.skidfuscator.ir.method.AcknowledgeUnlinkGroupException;
import dev.skidfuscator.ir.method.MethodGroup;
import dev.skidfuscator.ir.method.MethodTags;
import dev.skidfuscator.ir.method.MethodVisitor;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Java method wrapping class. A method is a group of
 * instructions with a maximum byte size of 65335 which
 * has a name, a set of parameters (also known as arguments),
 * and a return type. A method may be public, protected, private,
 * or none of these in visibility (same as private).
 * <br>
 * A method has a list of instructions and if not static,
 * belongs to a method group which handles hierarchy (overwritten
 * methods, interfaces, etc).
 */
public class Method extends MethodVisitor {
    protected Method() {
    }

    private Klass owner;
    private MethodGroup group;
    private String name;
    private Set<MethodTags> tags;
    private List<Klass> args;
    private Klass returnType;
    private AbstractInstructionList instructions;

    @Override
    public void visit(Klass owner, String name, Set<MethodTags> tags, List<Klass> args, Klass returnType) {
        this.setOwner(owner);

        try {
            this.setName(name);
            this.setArgs(args);
            this.setReturnType(returnType);
        } catch (AcknowledgeUnlinkGroupException ignored) {}

        this.tags = tags;

        super.visit(owner, name, tags, args, returnType);
    }

    @Override
    public void visit(MethodGroup group) {
        super.visit(group);
    }

    @Override
    public AbstractInstructionsVisitor visitCode() {
        return instructions;
    }

    /**
     * @return the parent Klass which owns this method
     */
    public @NotNull Klass getOwner() {
        return owner;
    }

    /**
     * Sets the parent klass which owns the method.
     *
     * @param owner Parent klass wrapper
     * @throws IllegalStateException if the owner is null
     */
    public void setOwner(@NotNull final Klass owner) {
        Assert.nonNull(owner, "Method owner cannot be null");

        this.owner = owner;
    }

    /**
     * @return  The method group it belongs to, null
     *          if the method has been manually removed
     *          from the group. If static, the method
     *          group will be unique.
     */
    public @Nullable MethodGroup getGroup() {
        return group;
    }

    /**
     * @param group Sets the group of the method. This
     *              is an unsafe method and should only
     *              really be accessed by MethodGroup
     *              or if you really know what you're
     *              doing.
     *
     * @see MethodGroup#addMethod(Method)
     * @see MethodGroup#removeMethod(Method)
     */
    protected void setGroup(@Nullable MethodGroup group) {
        this.group = group;
    }

    /**
     * @return The name of the method
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Sets name the name of the method. You should be
     * using MethodGroup#setName in most cases. Changing
     * the name will force the parent group to unlink. If
     * there is a parent group, the AcknowledgeUnlinkGroupException
     * exception will be thrown.
     *
     * @see MethodGroup#setArgs(List)
     *
     * @param name the new name of the method
     * @throws IllegalStateException if the name is null
     * @throws AcknowledgeUnlinkGroupException If there is a
     *         parent group, the exception will be thrown to
     *         force the developer to acknowledge this implicit
     *         change.
     */
    public void setName(@NotNull String name) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(name, "Cannot set null method name");

        this.name = name;

        if (group != null && !group.getName().equals(name)) {
            this.group.removeMethod(this);
            AcknowledgeUnlinkGroupException.check();
        }
    }

    /**
     * @return an unmodifiable collection of the arguments
     */
    public @NotNull List<Klass> getArgs() {
        return Collections.unmodifiableList(args);
    }

    /**
     * Sets the method arguments for this method. You should
     * be using MethodGroup#setArgs in most cases. Changing
     * the name will force the parent group to unlink. If
     * there is a parent group, the AcknowledgeUnlinkGroupException
     * exception will be thrown.
     *
     * @see MethodGroup#setArgs(List)
     *
     * @param args The method arguments for the method
     * @throws IllegalStateException if the arguments are null
     * @throws AcknowledgeUnlinkGroupException If there is a
     *         parent group, the exception will be thrown to
     *         force the developer to acknowledge this implicit
     *         change.
     */
    public void setArgs(@NotNull final List<Klass> args) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(args,"Method args cannot be null");

        this.args = args;
        this.group = null; // UNSAFE
        AcknowledgeUnlinkGroupException.check();
    }

    /**
     * @return The return type of the method
     */
    public @NotNull Klass getReturnType() {
        return returnType;
    }

    /**
     * Sets return type.
     *
     * @param returnType the return type
     * @throws AcknowledgeUnlinkGroupException the acknowledge unlink group exception
     */
    public void setReturnType(@NotNull final Klass returnType) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(returnType, "Return type cannot be null");

        this.returnType = returnType;
        this.group = null; // UNSAFE
        AcknowledgeUnlinkGroupException.check();
    }

    /**
     * @return whether the method is static
     */
    public boolean isStatic() {
        return tags.contains(MethodTags.STATIC);
    }

    /**
     * @return whether the method is an object initializer
     */
    public boolean isInit() {
        return name.equals("<init>");
    }

    public static MethodBuilder of() {
        return new MethodBuilder();
    }



    public static final class MethodBuilder {
        private Klass owner;
        private MethodGroup group;
        private String name;
        private Set<MethodTags> tags;
        private List<Klass> args;
        private Klass returnType;
        private AbstractInstructionList instructions;

        private MethodBuilder() {
        }

        public MethodBuilder owner(Klass owner) {
            this.owner = owner;
            return this;
        }

        public MethodBuilder group(MethodGroup group) {
            this.group = group;
            return this;
        }

        public MethodBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MethodBuilder tags(Set<MethodTags> tags) {
            this.tags = tags;
            return this;
        }

        public MethodBuilder args(List<Klass> args) {
            this.args = args;
            return this;
        }

        public MethodBuilder args(Klass... args) {
            this.args = Arrays.asList(args);
            return this;
        }

        public MethodBuilder returnType(Klass returnType) {
            this.returnType = returnType;
            return this;
        }

        public MethodBuilder instructions(AbstractInstructionList instructions) {
            this.instructions = instructions;
            return this;
        }

        public MethodBuilder but() {
            return of().owner(owner).group(group).name(name).tags(tags).args(args).returnType(returnType).instructions(instructions);
        }

        public Method build() {
            Assert.nonNull(owner, "Method owner cannot be null");
            Assert.nonNull(name, "Method name cannot be null");
            Assert.nonNull(args, "Method args cannot be null");
            Assert.nonNull(returnType, "Method return type cannot be null");
            Assert.nonNull(args, "Method parameters cannot be null (but can be empty!)");
            Assert.nonNull(tags, "Method tags cannot be null (but can be empty!)");
            Assert.nonNull(instructions, "Method instructions cannot be null (but can be empty!)");

            final Method method = new Method();
            method.visit(
                    owner,
                    name,
                    tags,
                    args,
                    returnType
            );
            method.instructions = this.instructions;
            return method;
        }
    }
}
