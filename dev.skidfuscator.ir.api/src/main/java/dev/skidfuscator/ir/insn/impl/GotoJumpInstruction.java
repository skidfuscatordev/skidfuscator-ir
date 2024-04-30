package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.GotoJumpInstructionVisitor;

public class GotoJumpInstruction extends AbstractJumpInstruction {
    protected GotoJumpInstruction() {
        super();
    }

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        this.copyTo(visitor.visitGotoJump());
    }

    public void copyTo(GotoJumpInstructionVisitor visitor) {
        visitor.copyFrom(this.getTarget());
    }

    public static Builder of() {
        return new Builder();
    }

    public static final class Builder {
        private LabelInstruction target;

        private Builder() {
        }

        public Builder target(LabelInstruction target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        public GotoJumpInstruction build() {
            final GotoJumpInstruction jumpInstruction = new GotoJumpInstruction();
            jumpInstruction.setTarget(target);

            return jumpInstruction;
        }
    }
}