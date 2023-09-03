package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.verify.Assert;

public interface Instruction {
    default void assertNotNull(final Object var) {
        Assert.nonNull(var, "Instruction is not initialized! Please visit it or use the builder!");
    }

    void copyTo(final InstructionsVisitor visitor);
}
