package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class MultiANewArrayInsn extends AbstractInsn {
    private final MultiANewArrayInsnNode node;
    private KlassNode target;

    public MultiANewArrayInsn(Hierarchy hierarchy, MultiANewArrayInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.target = hierarchy.findClass(Type.getObjectType(node.desc).getElementType().getClassName().replace(".", "/"));
    }

    @Override
    public AbstractInsnNode dump() {
        this.node.desc = "[L" + target.getName() + ";";
        return node;
    }
}
