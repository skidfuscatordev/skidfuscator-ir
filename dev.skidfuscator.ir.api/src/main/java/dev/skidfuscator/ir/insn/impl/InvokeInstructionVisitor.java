package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.Method;

public class InvokeInstructionVisitor extends InstructionVisitor {
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