package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.verify.Assert;

public class IincInstruction implements Instruction {
    private int local;
    private int increase;

    //TODO: Constructors

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        visitor.visitIinc(local, increase);
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        Assert.isTrue(local >= 0, "Local cannot be negative!");
        this.local = local;
    }

    public int getIncrease() {
        return increase;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }

    public static final class Builder {
        private int local;
        private int increase;

        private Builder() {
        }

        public Builder local(int local) {
            this.local = local;
            return this;
        }

        public Builder increase(int increase) {
            this.increase = increase;
            return this;
        }

        public Builder but() {
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
