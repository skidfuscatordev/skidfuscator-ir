package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class ConstantInsn extends AbstractInsn {
    protected Object constant;

    public ConstantInsn(Hierarchy hierarchy, AbstractInsnNode node) {
        super(hierarchy, node);
    }

    public void setConstant(final Object constant) {
        this.constant = constant;
    }

    public Object getConstant() {
        return constant;
    }
}
