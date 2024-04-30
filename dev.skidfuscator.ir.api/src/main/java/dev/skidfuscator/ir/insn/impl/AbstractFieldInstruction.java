package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.impl.visitor.AbstractFieldInstructionVisitor;
import dev.skidfuscator.ir.verify.Assert;

public sealed abstract class AbstractFieldInstruction extends AbstractFieldInstructionVisitor implements Instruction permits GetFieldInstruction, SetFieldInstruction {
    protected Field target;

    protected AbstractFieldInstruction() {
        super();
    }

    public AbstractFieldInstruction(AbstractFieldInstructionVisitor next) {
        super(next);
    }

    public static GetFieldInstruction.Builder ofGet() {
        return GetFieldInstruction.of();
    }

    public static SetFieldInstruction.Builder ofSet() {
        return SetFieldInstruction.of();
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

    public Field getTarget() {
        return target;
    }

    public void setTarget(Field target) {
        Assert.nonNull(target, "Cannot set field target to null!");
        this.target = target;
    }
}
