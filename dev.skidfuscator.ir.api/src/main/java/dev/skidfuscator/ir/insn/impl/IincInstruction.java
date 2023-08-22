package dev.skidfuscator.ir.insn.impl;

public class IincInstruction extends AbstractInstruction {
    private int local;
    private int increase;

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    public int getIncrease() {
        return increase;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        visitor.visitIinc(local, increase);
    }

    public static IincInstructionBuilder of() {
        return new IincInstructionBuilder();
    }

    public static final class IincInstructionBuilder {
        private int local;
        private int increase;

        private IincInstructionBuilder() {
        }

        public IincInstructionBuilder local(int local) {
            this.local = local;
            return this;
        }

        public IincInstructionBuilder increase(int increase) {
            this.increase = increase;
            return this;
        }

        public IincInstructionBuilder but() {
            return of().local(local).increase(increase);
        }

        public IincInstruction build() {
            IincInstruction iincInstruction = new IincInstruction();
            iincInstruction.setLocal(local);
            iincInstruction.setIncrease(increase);
            return iincInstruction;
        }
    }
}
