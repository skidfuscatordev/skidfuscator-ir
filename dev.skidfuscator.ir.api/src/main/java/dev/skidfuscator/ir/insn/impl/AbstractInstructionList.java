package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.insn.Instruction;

import java.util.List;

public class AbstractInstructionList extends AbstractInstructionsVisitor {
    private final List<Instruction> instructions;

    public AbstractInstructionList(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public ArithmeticInstructionVisitor visitArithmetic() {
        final ArithmeticInstruction instruction = new ArithmeticInstruction(super.visitArithmetic());
        this.instructions.add(instruction);
        return instruction;
    }

    @Override
    public AbstractFieldInstructionVisitor visitSetField() {
        final SetFieldInstruction fieldInstruction = new SetFieldInstruction(super.visitSetField());
        this.instructions.add(fieldInstruction);

        return fieldInstruction;
    }

    @Override
    public AbstractFieldInstructionVisitor visitGetField() {
        final GetFieldInstruction fieldInstruction = new GetFieldInstruction(super.visitSetField());
        this.instructions.add(fieldInstruction);

        return fieldInstruction;
    }

    @Override
    public void visitIinc(int local, int increment) {
        super.visitIinc(local, increment);
    }

    @Override
    public InvokeInstructionVisitor visitInvoke() {
        return super.visitInvoke();
    }

    @Override
    public void visitLabel(int offset) {
        super.visitLabel(offset);
    }

    @Override
    public ConstantInstructionVisitor visitConstant() {
        final ConstantInstruction constantInstruction = new ConstantInstruction();
        this.instructions.add(constantInstruction);

        return constantInstruction;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
