package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.AbstractFieldInstructionVisitor;

public non-sealed class GetFieldInstruction extends AbstractFieldInstruction {
    protected GetFieldInstruction() {
        super();
    }

    public GetFieldInstruction(AbstractFieldInstructionVisitor next) {
        super(next);
    }

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        this.copyTo(visitor.visitGetField());
    }

    public static final class Builder {
        private Field target;

        private Builder() {
        }

        public Builder target(Field target) {
            this.target = target;
            return this;
        }

        public Builder but() {
            return of().target(target);
        }

        public GetFieldInstruction build() {
            final GetFieldInstruction fieldInstruction = new GetFieldInstruction();
            fieldInstruction.setTarget(target);

            return fieldInstruction;
        }
    }
}
