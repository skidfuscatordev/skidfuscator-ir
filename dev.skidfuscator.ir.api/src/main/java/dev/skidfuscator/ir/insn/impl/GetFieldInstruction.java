package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;

public class GetFieldInstruction extends AbstractFieldInstruction {
    private GetFieldInstruction(final Field target) {
        super(target);
    }

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        visitor.visitGetField(target);
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
            return GetFieldInstruction.of();
        }

        public Builder target(Field target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        @Override
        public GetFieldInstruction build() {
            final GetFieldInstruction fieldInstruction = new GetFieldInstruction(target);
            return fieldInstruction;
        }
    }
}
