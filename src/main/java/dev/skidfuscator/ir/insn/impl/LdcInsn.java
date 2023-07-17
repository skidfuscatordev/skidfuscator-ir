package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.ConstantInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LdcInsnNode;

//TODO: I dont know how i should implement ldc with type, even more when it comes to array type
public class LdcInsn extends ConstantInsn {
    private final LdcInsnNode node;

    public LdcInsn(Hierarchy hierarchy, LdcInsnNode node) {
        super(hierarchy);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.constant = this.constant instanceof Type type ? hierarchy.findClass(type.getInternalName()) : node.cst;
    }

    @Override
    public void dump() {
        this.node.cst = this.constant instanceof KlassNode target ? target.asType() : this.constant;
    }
}
