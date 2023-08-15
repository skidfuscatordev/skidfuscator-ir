package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.impl.ArithmeticInstruction;
import dev.skidfuscator.ir.primitive.Primitive;

public abstract class InstructionVisitor {
    private InstructionVisitor visitor;

    public InstructionVisitor() {
        this(null);
    }

    public InstructionVisitor(InstructionVisitor visitor) {
        this.visitor = visitor;
    }

    public void visitArithmetic(final Primitive primitive, final ArithmeticInstruction.Operation operation) {
        if (visitor != null)
            visitor.visitArithmetic(primitive, operation);
    }

    public void visitSetField(final Field target) {
        if (visitor != null)
            visitor.visitSetField(target);
    }

    public void visitGetField(final Field target) {
        if (visitor != null)
            visitor.visitGetField(target);
    }

    public void visitIinc(final int local, final int increment) {
        if (visitor != null)
            visitor.visitIinc(local, increment);
    }

    public void visitInvoke(final Method target) {
        if (visitor != null)
            visitor.visitInvoke(target);
    }

    public void visitLabel(final int offset) {
        if (visitor != null) {
            visitor.visitLabel(offset);
        }
    }

    public void visitConstant(final Object constant) {
        if (visitor != null) {
            visitor.visitConstant(constant);
        }
    }
}
