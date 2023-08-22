package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.primitive.Primitive;

public class ArithmeticInstructionVisitor {
    private ArithmeticInstructionVisitor next;

    public ArithmeticInstructionVisitor() {
    }

    public ArithmeticInstructionVisitor(ArithmeticInstructionVisitor parent) {
        this.next = parent;
    }

    public void copyFrom(final Primitive type, final ArithmeticInstruction.Operation operation) {
        if (next != null)
            next.copyFrom(type, operation);
    }
}
