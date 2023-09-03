package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.ConstantInstructionVisitor;

public class ConstantInstruction extends ConstantInstructionVisitor implements Instruction {
    private Object constant;

    protected ConstantInstruction() {
        super();
    }

    public ConstantInstruction(Object constant) {
        this.constant = constant;
    }

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        this.copyTo(visitor.visitConstant());
    }

    public void copyTo(final ConstantInstructionVisitor visitor) {
        visitor.copyFrom(constant);
    }

    @Override
    public void copyFrom(Object constant) {
        this.setConstant(constant);
        super.copyFrom(constant);
    }

    public void copyFrom(final ConstantInstruction visitor) {
        visitor.copyTo(this);
    }

    /**
     * @return Constant provided by the instruction.
     */
    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    public static final class Builder {
        private Object constant;

        private Builder() {
        }

        public Builder constant(Object constant) {
            this.constant = constant;
            return this;
        }

        public Builder but() {
            return of().constant(constant);
        }

        public ConstantInstruction build() {
            final ConstantInstruction constantInstruction = new ConstantInstruction();
            constantInstruction.setConstant(constant);

            return constantInstruction;
        }
    }
}
