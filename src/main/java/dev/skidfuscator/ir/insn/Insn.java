package dev.skidfuscator.ir.insn;

import org.objectweb.asm.tree.AbstractInsnNode;

public interface Insn<T extends AbstractInsnNode> {
    void resolve();

    int getOpcode();

    T dump();

    InstructionList getParent();

    void setParent(InstructionList parent);

    void replace(final Insn<?>... insns);
}
