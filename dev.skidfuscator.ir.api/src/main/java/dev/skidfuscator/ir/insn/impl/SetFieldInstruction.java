package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.AbstractInstruction;
import dev.skidfuscator.ir.insn.InstructionVisitor;

public class SetFieldInstruction extends AbstractInstruction {
    private Field target;

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        this.target = target;
    }

    @Override
    public void visit(InstructionVisitor visitor) {
        visitor.visitSetField(target);
    }
}
