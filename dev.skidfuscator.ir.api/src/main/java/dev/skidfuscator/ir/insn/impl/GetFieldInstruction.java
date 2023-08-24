package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;

public class GetFieldInstruction extends AbstractFieldInstruction {
    protected GetFieldInstruction() {
        super();
    }

    protected GetFieldInstruction(AbstractFieldInstructionVisitor next) {
        super(next);
    }

    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        this.copyTo(visitor.visitGetField());
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
            final GetFieldInstruction fieldInstruction = new GetFieldInstruction();
            fieldInstruction.setTarget(target);

            return fieldInstruction;
        }
    }
}
