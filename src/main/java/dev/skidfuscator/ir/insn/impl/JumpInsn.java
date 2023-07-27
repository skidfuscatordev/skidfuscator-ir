package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

public class JumpInsn extends AbstractInsn<JumpInsnNode> {
    private LabelInsn label;

    public JumpInsn(Hierarchy hierarchy, JumpInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        this.label = getParent().getLabel(node.label);
        super.resolve();
    }

    @Override
    public JumpInsnNode dump() {
        this.node.label = label.dump();

        return super.dump();
    }

    public LabelInsn getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "jump " + node.label.getLabel();
    }
}
