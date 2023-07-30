package dev.skidfuscator.ir.insn;

import org.objectweb.asm.tree.AbstractInsnNode;

public interface Insn<T extends AbstractInsnNode> {
    void resolve();

    void lock();

    int getOpcode();

    T dump();

    InstructionList getParent();

    void setParent(InstructionList parent);

    void replace(final Insn<?>... insns);
}
