package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.insn.impl.visitor.AbstractFieldInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.ArithmeticInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.ConstantInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.InvokeInstructionVisitor;

/**
 * TODO: Switch all of these to instead refer the instruction,
 * then create dedicated visitors.
 */
public abstract class InstructionsVisitor {
    private final InstructionsVisitor visitor;

    public InstructionsVisitor() {
        this(null);
    }

    public InstructionsVisitor(InstructionsVisitor visitor) {
        this.visitor = visitor;
    }

    public ArithmeticInstructionVisitor visitArithmetic() {
        if (visitor != null)
            return visitor.visitArithmetic();

        return null;
    }

    public AbstractFieldInstructionVisitor visitSetField() {
        if (visitor != null)
            return visitor.visitSetField();

        return null;
    }

    public AbstractFieldInstructionVisitor visitGetField() {
        if (visitor != null)
            return visitor.visitGetField();

        return null;
    }

    public void visitIinc(final int local, final int increment) {
        if (visitor != null)
            visitor.visitIinc(local, increment);
    }

    public InvokeInstructionVisitor visitInvoke() {
        if (visitor != null)
            return visitor.visitInvoke();

        return null;
    }

    public void visitLabel(final int offset) {
        if (visitor != null)
            visitor.visitLabel(offset);
    }

    public ConstantInstructionVisitor visitConstant() {
        if (visitor != null)
            return visitor.visitConstant();

        return null;
    }
}
