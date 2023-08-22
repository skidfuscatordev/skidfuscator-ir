package dev.skidfuscator.ir.insn.impl;

public class ConstantInstruction extends AbstractInstruction {
    private Object constant;

    private ConstantInstruction(Object constant) {
        this.constant = constant;
    }

    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        visitor.visitConstant(constant);
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
            return new ConstantInstruction(constant);
        }
    }
}
