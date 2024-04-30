package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.InstructionVisitor;

public abstract class AbstractFieldInstructionVisitor implements InstructionVisitor {
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
