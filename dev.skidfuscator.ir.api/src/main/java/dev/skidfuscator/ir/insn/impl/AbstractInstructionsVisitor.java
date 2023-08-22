package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.primitive.Primitive;

/**
 * TODO: Switch all of these to instead refer the instruction,
 * then create dedicated visitors.
 */
public abstract class AbstractInstructionsVisitor {
    private AbstractInstructionsVisitor visitor;

    public AbstractInstructionsVisitor() {
        this(null);
    }

    public AbstractInstructionsVisitor(AbstractInstructionsVisitor visitor) {
        this.visitor = visitor;
    }

    protected ArithmeticInstructionVisitor visitArithmetic() {
        if (visitor != null) {
            return visitor.visitArithmetic();
        }

        return null;
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

    public InvokeInstructionVisitor visitInvoke() {
        if (visitor != null)
            return new InvokeInstruction(visitor.visitInvoke());

        return new InvokeInstruction();
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
