package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.verify.Assert;

public abstract class AbstractJumpInstruction implements Instruction {
    private LabelInstruction targetLabel;

    protected AbstractJumpInstruction() {
    }

    public LabelInstruction getTarget() {
        assertNotNull(targetLabel);

        return targetLabel;
    }

    public void setTarget(LabelInstruction targetLabel) {
        Assert.nonNull(targetLabel, "Target label cannot be null");
        this.targetLabel = targetLabel;
    }
}