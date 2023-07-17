package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsn implements Insn {
    protected final Hierarchy hierarchy;
    protected final int opcode;

    public AbstractInsn(Hierarchy hierarchy, AbstractInsnNode node) {
        this.hierarchy = hierarchy;
        this.opcode = node.getOpcode();
    }

    public int getOpcode() {
        return opcode;
    }
}
