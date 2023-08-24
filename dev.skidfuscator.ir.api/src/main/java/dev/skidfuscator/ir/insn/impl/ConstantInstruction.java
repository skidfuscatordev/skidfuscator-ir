package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;

public class ConstantInstruction extends ConstantInstructionVisitor implements Instruction {
    private Object constant;

    protected ConstantInstruction() {
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

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
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

    public static ConstantInstructionBuilder of() {
        return new ConstantInstructionBuilder();
    }

    public static final class ConstantInstructionBuilder {
        private Object constant;

        private ConstantInstructionBuilder() {
        }

        public ConstantInstructionBuilder constant(Object constant) {
            this.constant = constant;
            return this;
        }

        public ConstantInstructionBuilder but() {
            return of().constant(constant);
        }

        public ConstantInstruction build() {
            final ConstantInstruction constantInstruction = new ConstantInstruction();
            constantInstruction.setConstant(constant);

            return constantInstruction;
        }
    }
}
