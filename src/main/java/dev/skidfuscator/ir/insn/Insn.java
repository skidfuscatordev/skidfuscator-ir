package dev.skidfuscator.ir.insn;

import org.objectweb.asm.tree.AbstractInsnNode;

public interface Insn {
    void resolve();

    AbstractInsnNode dump();
}
