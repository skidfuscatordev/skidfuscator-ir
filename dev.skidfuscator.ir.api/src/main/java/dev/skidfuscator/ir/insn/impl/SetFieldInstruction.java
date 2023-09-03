package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.AbstractFieldInstructionVisitor;

public non-sealed class SetFieldInstruction extends AbstractFieldInstruction {
    protected SetFieldInstruction() {
        super();
    }

    public SetFieldInstruction(AbstractFieldInstructionVisitor next) {
        super(next);
    }


    public static Builder of() {
        return new Builder();
    }

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        this.copyTo(visitor.visitSetField());
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

        public SetFieldInstruction build() {
            final SetFieldInstruction fieldInstruction = new SetFieldInstruction();
            fieldInstruction.setTarget(target);

            return fieldInstruction;
        }
    }
}
