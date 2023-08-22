package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.method.Method;

public class InvokeInstructionVisitor extends InstructionVisitor {
    private InvokeInstructionVisitor next;

    public InvokeInstructionVisitor() {
    }

    public InvokeInstructionVisitor(InvokeInstructionVisitor parent) {
        this.next = parent;
    }

    public void visit(final Method target) {
        if (next != null)
            next.visit(target);
    }
}