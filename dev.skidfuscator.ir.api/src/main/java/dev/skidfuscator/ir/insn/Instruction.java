package dev.skidfuscator.ir.insn;

public interface Instruction {
    void visit(final InstructionVisitor visitor);
}
