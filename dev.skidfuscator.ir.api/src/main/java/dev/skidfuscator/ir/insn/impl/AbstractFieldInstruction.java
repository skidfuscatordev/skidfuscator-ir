package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;

public abstract class AbstractFieldInstruction extends AbstractInstruction {
    protected Field target;

    protected AbstractFieldInstruction(Field target) {
        this.target = target;
    }

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        this.target = target;
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
