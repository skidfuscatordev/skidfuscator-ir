package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

public class InvokeInstruction extends InvokeInstructionVisitor implements Instruction {

    public InvokeInstruction() {
    }

    public InvokeInstruction(InvokeInstructionVisitor parent) {
        super(parent);
    }

    private Method target;

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

    @Override
    public void copyTo(final AbstractInstructionsVisitor visitor) {
        visitor.visitInvoke().copyFrom(target);
    }

    @Override
    public void copyFrom(final Method target) {
        this.target = target;

        super.copyFrom(target);
    }


    public void visit(final InvokeInstructionVisitor visitor) {
        visitor.copyFrom(target);
    }

    public static InvokeInstructionBuilder of() {
        return new InvokeInstructionBuilder();
    }


    public static final class InvokeInstructionBuilder {
        private Method target;

        private InvokeInstructionBuilder() {
        }

        public InvokeInstructionBuilder target(Method target) {
            this.target = target;
            return this;
        }

        public InvokeInstructionBuilder but() {
            return of().target(target);
        }

        public InvokeInstruction build() {
            InvokeInstruction invokeInstruction = new InvokeInstruction();
            invokeInstruction.setTarget(target);
            return invokeInstruction;
        }
    }
}
