package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.insn.impl.LabelInstruction;

public class GotoJumpInstructionVisitor implements InstructionVisitor {
    private GotoJumpInstructionVisitor next;

    public GotoJumpInstructionVisitor() {
    }

    public GotoJumpInstructionVisitor(GotoJumpInstructionVisitor next) {
        this.next = next;
    }

    public void copyFrom(final LabelInstruction target) {
        if (next != null)
            next.copyFrom(target);
    }
}
