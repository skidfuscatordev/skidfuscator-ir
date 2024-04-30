package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionsVisitor;

public class LabelInstruction implements Instruction {
    private int offset;

    @Override
    public void copyTo(InstructionsVisitor visitor) {
        visitor.visitLabel().copyTo(visitor);
    }
}
