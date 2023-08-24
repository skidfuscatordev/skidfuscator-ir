package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.Instruction;

public abstract class AbstractFieldInstruction extends AbstractFieldInstructionVisitor implements Instruction {
    protected Field target;

    protected AbstractFieldInstruction() {
    }

    protected AbstractFieldInstruction(AbstractFieldInstructionVisitor next) {
        super(next);
    }

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        this.target = target;
    }

    public void copyTo(final AbstractFieldInstructionVisitor visitor) {
        visitor.copyFrom(target);
    }

    @Override
    public void copyFrom(final Field target) {
        this.setTarget(target);

        super.copyFrom(target);
    }

    public void copyFrom(final AbstractFieldInstruction visitor) {
        visitor.copyTo(this);
    }

    public static abstract class Builder {
        private Field target;

        protected Builder() {
        }

        public abstract Builder of();

        public Builder target(Field target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        public abstract AbstractFieldInstruction build();
    }
}
