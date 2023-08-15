package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.method.dynamic.Dynamic;
import dev.skidfuscator.ir.method.dynamic.DynamicHandle;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

public class InvokeDynamicInsn extends AbstractInsn<InvokeDynamicInsnNode> {

    private final Dynamic<InvokeDynamicInsnNode> dynamic;
    public InvokeDynamicInsn(Hierarchy hierarchy, InvokeDynamicInsnNode node) {
        super(hierarchy, node);
        this.node = node;
        this.dynamic = new Dynamic<>(node, hierarchy);
    }

    @Override
    public void resolve() {
        dynamic.resolveHierarchy();
        super.resolve();
    }

    @Override
    public InvokeDynamicInsnNode dump() {
        dynamic.dump();
        return super.dump();
    }

    public Dynamic<InvokeDynamicInsnNode> getDynamic() {
        return dynamic;
    }

    @Override
    public String toString() {
        return "push(indy) " + node.name + node.desc;
    }
}
