package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

public abstract class AbstractConstantInsn<T extends AbstractInsnNode> extends AbstractInsn<T> {
    protected Object constant;

    public AbstractConstantInsn(Hierarchy hierarchy, T node) {
        super(hierarchy, node);
    }

    public void setConstant(final Object constant) {
        this.constant = constant;
    }

    public Object getConstant() {
        return constant;
    }
}
