package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.insn.impl.ArithmeticInstruction;
import dev.skidfuscator.ir.insn.impl.ConstantInstruction;
import dev.skidfuscator.ir.insn.impl.GetFieldInstruction;
import dev.skidfuscator.ir.insn.impl.SetFieldInstruction;
import dev.skidfuscator.ir.insn.impl.visitor.AbstractFieldInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.ArithmeticInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.ConstantInstructionVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.InvokeInstructionVisitor;

import java.util.List;

public class InstructionList extends InstructionsVisitor {
    private final List<Instruction> instructions;

    public InstructionList(List<Instruction> instructions) {
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
        final ConstantInstruction constantInstruction = new ConstantInstruction(super.visitConstant());
        this.instructions.add(constantInstruction);

        return constantInstruction;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
