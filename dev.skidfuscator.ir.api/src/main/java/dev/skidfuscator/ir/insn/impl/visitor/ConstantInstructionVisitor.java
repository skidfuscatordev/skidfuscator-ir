package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.insn.InstructionVisitor;

public class ConstantInstructionVisitor implements InstructionVisitor {
    private ConstantInstructionVisitor next;

    public ConstantInstructionVisitor() {
    }

    public ConstantInstructionVisitor(ConstantInstructionVisitor next) {
        this.next = next;
    }

    public void copyFrom(final Object constant) {
        if (next != null)
            next.copyFrom(constant);
    }
}
