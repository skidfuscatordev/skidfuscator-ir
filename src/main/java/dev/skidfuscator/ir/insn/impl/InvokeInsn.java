package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.FunctionNode;
//import dev.skidfuscator.ir.gen.BytecodeFrontend;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;

public class InvokeInsn extends AbstractInsn {
    private final MethodInsnNode node;
    private FunctionNode target;

    public InvokeInsn(Hierarchy hierarchy, MethodInsnNode node) {
        super(hierarchy);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.target = hierarchy.findMethod(node);
    }

    @Override
    public void dump() {
        this.node.owner = target.getParent().getName();
        this.node.name = target.getName();
        this.node.desc = target.getDesc();
    }

    public boolean isStatic() {
        return target.isStatic();
    }
}
