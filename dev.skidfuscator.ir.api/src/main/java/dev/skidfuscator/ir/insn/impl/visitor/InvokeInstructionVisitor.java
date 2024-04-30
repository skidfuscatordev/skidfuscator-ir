package dev.skidfuscator.ir.insn.impl.visitor;

import dev.skidfuscator.ir.JavaMethod;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

public class InvokeInstructionVisitor implements InstructionVisitor {
    private InvokeInstructionVisitor next;

    public InvokeInstructionVisitor() {
    }

    public InvokeInstructionVisitor(InvokeInstructionVisitor parent) {
        this.next = parent;
    }

    public void copyFrom(@NotNull final Method target) {
        // Method target cannot be null using Assert.nonNull
        Assert.nonNull(target, "Cannot set invocation target to null!");

        if (next != null)
            next.copyFrom(target);
    }

}