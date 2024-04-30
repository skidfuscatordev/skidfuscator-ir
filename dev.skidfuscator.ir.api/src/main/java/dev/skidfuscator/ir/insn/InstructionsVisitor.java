package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.insn.impl.GotoJumpInstruction;
import dev.skidfuscator.ir.insn.impl.LabelInstruction;
import dev.skidfuscator.ir.insn.impl.visitor.*;

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

    public LabelInstruction visitLabel() {
        if (visitor != null)
            return visitor.visitLabel();

        return null;
    }

    public ConstantInstructionVisitor visitConstant() {
        if (visitor != null)
            return visitor.visitConstant();

        return null;
    }

    public GotoJumpInstructionVisitor visitGotoJump() {
        if (visitor != null)
            return visitor.visitGotoJump();

        return null;
    }
}
