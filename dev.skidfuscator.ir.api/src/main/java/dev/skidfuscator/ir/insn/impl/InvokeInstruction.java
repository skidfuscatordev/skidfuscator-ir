package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.JavaMethod;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.InvokeInstructionVisitor;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

public class InvokeInstruction extends InvokeInstructionVisitor implements Instruction {

    private Method target;

    protected InvokeInstruction() {
        super();
    }

    public InvokeInstruction(InvokeInstructionVisitor parent) {
        super(parent);
    }

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void copyTo(final InstructionsVisitor visitor) {
        // Assert visitor is not null
        Assert.nonNull(visitor, "Cannot copy to null visitor!");

        final InvokeInstructionVisitor invokeVisitor = visitor.visitInvoke();
        Assert.nonNull(invokeVisitor, "Cannot copy to null invoke visitor!");

        // Assert target is not null
        Assert.nonNull(target, "Cannot copy from null target!");

        invokeVisitor.copyFrom(target);
    }

    @Override
    public void copyFrom(final @NotNull Method target) {
        this.target = target;
        super.copyFrom(target);
    }

    public void visit(final InvokeInstructionVisitor visitor) {
        visitor.copyFrom(target);
    }

    public Method getTarget() {
        return target;
    }

    public void setTarget(@NotNull final Method target) {
        Assert.nonNull(target, "Cannot set invocation target to null!");

        this.target = target;
    }

    public boolean isStatic() {
        return target.isStatic();
    }

    public static final class Builder {
        private Method target;

        private Builder() {
        }

        public Builder target(Method target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        public InvokeInstruction build() {
            InvokeInstruction invokeInstruction = new InvokeInstruction();
            invokeInstruction.setTarget(target);
            return invokeInstruction;
        }
    }
}
