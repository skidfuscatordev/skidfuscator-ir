package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsn extends AbstractInsn<VarInsnNode> {
    public VarInsn(Hierarchy hierarchy, VarInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public String toString() {
        return switch (this.getOpcode()) {
            case Opcodes.ILOAD -> "iload " + node.var;
            case Opcodes.LLOAD -> "lload " + node.var;
            case Opcodes.FLOAD -> "fload " + node.var;
            case Opcodes.DLOAD -> "dload " + node.var;
            case Opcodes.ALOAD -> "aload " + node.var;
            case Opcodes.ISTORE -> "istore " + node.var;
            case Opcodes.LSTORE -> "lstore " + node.var;
            case Opcodes.FSTORE -> "fstore " + node.var;
            case Opcodes.DSTORE -> "dstore " + node.var;
            case Opcodes.ASTORE -> "astore " + node.var;
            case Opcodes.RET -> "ret " + node.var;
            default -> "unknown var " + node.var;
        };
    }
}
