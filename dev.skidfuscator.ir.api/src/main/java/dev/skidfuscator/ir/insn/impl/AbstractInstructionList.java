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
    public void visitSetField(Field target) {
        this.instructions.add(SetFieldInstruction.of()
                .target(target)
                .build()
        );

        super.visitSetField(target);
    }

    @Override
    public void visitGetField(Field target) {
        this.instructions.add(GetFieldInstruction.of()
                .target(target)
                .build()
        );

        super.visitGetField(target);
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
    public void visitConstant(Object constant) {
        super.visitConstant(constant);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
