package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;

public class SetFieldInstruction extends AbstractFieldInstruction {
    private SetFieldInstruction(Field target) {
        super(target);
    }

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        visitor.visitSetField(target);
    }

    public static Builder of() {
        return new Builder();
    }

    public static final class Builder extends AbstractFieldInstruction.Builder {
        private Field target;

        private Builder() {
        }

        @Override
        public Builder of() {
            return SetFieldInstruction.of();
        }

        public Builder target(Field target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        @Override
        public SetFieldInstruction build() {
            final SetFieldInstruction fieldInstruction = new SetFieldInstruction(target);
            return fieldInstruction;
        }
    }
}
