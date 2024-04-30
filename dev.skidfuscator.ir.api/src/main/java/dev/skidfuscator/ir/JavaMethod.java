package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.impl.MethodModifier;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.method.AcknowledgeUnlinkGroupException;
import dev.skidfuscator.ir.method.MethodGroup;
import dev.skidfuscator.ir.method.MethodVisitor;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class JavaMethod extends MethodVisitor implements Method {
    private Klass owner;
    private MethodGroup group;
    private String name;
    private MethodModifier modifier;
    private List<Klass> args;
    private Klass returnType;
    private InstructionList instructions;

    public JavaMethod() {
    }

    private JavaMethod(Klass owner, MethodGroup group, String name, MethodModifier modifier, List<Klass> args, Klass returnType, InstructionList instructions) {
        this.owner = owner;
        this.group = group;
        this.name = name;
        this.modifier = modifier;
        this.args = args;
        this.returnType = returnType;
        this.instructions = instructions;
    }

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void visit(Klass owner, String name, MethodModifier modifier, List<Klass> args, Klass returnType) {
        this.setOwner(owner);

        try {
            this.setName(name);
            this.setArgs(args);
            this.setReturnType(returnType);
        } catch (AcknowledgeUnlinkGroupException ignored) {
        }

        this.modifier = modifier;

        super.visit(owner, name, modifier, args, returnType);
    }

    @Override
    public void visitGroup(MethodGroup group) {
        super.visitGroup(group);
    }

    @Override
    public InstructionsVisitor visitCode() {
        return instructions;
    }

    @Override
    public @NotNull Klass getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@NotNull final Klass owner) {
        Assert.nonNull(owner, "Method owner cannot be null");

        this.owner = owner;
    }

    @Override
    public @Nullable MethodGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(@Nullable MethodGroup group) {
        this.group = group;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(name, "Cannot set null method name");

        this.name = name;

        if (group != null && !group.getName().equals(name)) {
            this.group.removeMethod(this);
            AcknowledgeUnlinkGroupException.check();
        }
    }

    @Override
    public @NotNull List<Klass> getArgs() {
        return Collections.unmodifiableList(args);
    }

    @Override
    public void setArgs(@NotNull final List<Klass> args) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(args, "Method args cannot be null");

        this.args = args;
        this.group = null; // UNSAFE
        AcknowledgeUnlinkGroupException.check();
    }

    @Override
    public @NotNull Klass getReturnType() {
        return returnType;
    }

    @Override
    public void setReturnType(@NotNull final Klass returnType) throws AcknowledgeUnlinkGroupException {
        Assert.nonNull(returnType, "Return type cannot be null");

        this.returnType = returnType;
        this.group = null; // UNSAFE
        AcknowledgeUnlinkGroupException.check();
    }

    @Override
    public boolean isInit() {
        return name.equals("<init>");
    }

    @Override
    public boolean isClassInit() {
        return name.equals("<clinit>");
    }

    @Override
    public MethodModifier getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(MethodModifier modifier) {
        this.modifier = modifier;
    }


    public static final class Builder {
        private Klass owner;
        private MethodGroup group;
        private String name;
        private MethodModifier modifier;
        private List<Klass> args;
        private Klass returnType;
        private InstructionList instructions;

        private Builder() {
        }

        public Builder owner(Klass owner) {
            this.owner = owner;
            return this;
        }

        public Builder group(MethodGroup group) {
            this.group = group;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder modifier(MethodModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public Builder args(List<Klass> args) {
            this.args = args;
            return this;
        }

        public Builder args(Klass... args) {
            this.args = Arrays.asList(args);
            return this;
        }

        public Builder returnType(Klass returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder instructions(InstructionList instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder but() {
            return of().owner(owner).group(group).name(name).modifier(modifier).args(args).returnType(returnType).instructions(instructions);
        }

        public JavaMethod build() {
            Assert.nonNull(owner, "Method owner cannot be null");
            Assert.nonNull(name, "Method name cannot be null");
            Assert.nonNull(args, "Method args cannot be null");
            Assert.nonNull(returnType, "Method return type cannot be null");
            Assert.nonNull(args, "Method parameters cannot be null (but can be empty!)");
            Assert.nonNull(modifier, "Method modifier cannot be null (but can be empty!)");
            Assert.nonNull(instructions, "Method instructions cannot be null (but can be empty!)");
            return new JavaMethod(owner, group, name, modifier, args, returnType, instructions);
        }
    }
}
