package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.arithmetic.ArithmeticOperation;
import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.primitive.Primitive;

public class ArithmeticInstructionVisitor implements InstructionVisitor {
    private ArithmeticInstructionVisitor next;

    public ArithmeticInstructionVisitor() {
    }

    public ArithmeticInstructionVisitor(ArithmeticInstructionVisitor parent) {
        this.next = parent;
    }

    public void copyFrom(final Primitive type, final ArithmeticOperation operation) {
        if (next != null)
            next.copyFrom(type, operation);
    }
}
