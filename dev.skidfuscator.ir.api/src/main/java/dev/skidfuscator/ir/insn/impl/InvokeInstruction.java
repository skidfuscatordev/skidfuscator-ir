package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.AbstractInstruction;
import dev.skidfuscator.ir.insn.InstructionVisitor;

public class InvokeInstruction extends AbstractInstruction {
    private Method target;

    public boolean isStatic() {
        return target.isStatic();
    }

    @Override
    public void visit(InstructionVisitor visitor) {
        visitor.visitInvoke(target);
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
            invokeInstruction.target = this.target;
            return invokeInstruction;
        }
    }
}
