package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.AbstractInstruction;
import dev.skidfuscator.ir.insn.InstructionVisitor;

public class GetFieldInstruction extends AbstractInstruction {
    private Field target;

    private GetFieldInstruction(Field target) {
        this.target = target;
    }

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        this.target = target;
    }

    @Override
    public void visit(InstructionVisitor visitor) {
        visitor.visitGetField(target);
    }

    public static GetFieldInstructionBuilder of() {
        return new GetFieldInstructionBuilder();
    }

    public static final class GetFieldInstructionBuilder {
        private Field target;

        private GetFieldInstructionBuilder() {
        }

        public GetFieldInstructionBuilder target(Field target) {
            this.target = target;
            return this;
        }

        public GetFieldInstructionBuilder but() {
            return of().target(target);
        }

        public GetFieldInstruction build() {
            return new GetFieldInstruction(target);
        }
    }
}
