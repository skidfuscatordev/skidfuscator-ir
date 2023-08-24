package dev.skidfuscator.ir.insn.impl;

public class ConstantInstructionVisitor {
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
