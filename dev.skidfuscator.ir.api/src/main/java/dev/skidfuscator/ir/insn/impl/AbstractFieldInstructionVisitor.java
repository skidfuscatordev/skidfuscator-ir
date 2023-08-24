package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;

public class AbstractFieldInstructionVisitor {
    private AbstractFieldInstructionVisitor next;

    public AbstractFieldInstructionVisitor() {
    }

    public AbstractFieldInstructionVisitor(AbstractFieldInstructionVisitor next) {
        this.next = next;
    }

    public void copyFrom(final Field target) {
        if (next != null)
            next.copyFrom(target);
    }
}
