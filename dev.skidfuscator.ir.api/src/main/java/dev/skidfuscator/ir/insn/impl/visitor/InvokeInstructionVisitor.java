package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.InstructionVisitor;

public class InvokeInstructionVisitor implements InstructionVisitor {
    private InvokeInstructionVisitor next;

    public InvokeInstructionVisitor() {
    }

    public InvokeInstructionVisitor(InvokeInstructionVisitor parent) {
        this.next = parent;
    }

    public void copyFrom(final Method target) {
        if (next != null)
            next.copyFrom(target);
    }
}