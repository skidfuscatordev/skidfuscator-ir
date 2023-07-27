package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.ConstantInsn;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

//TODO: I dont know how i should implement ldc with type, even more when it comes to array type
public class LdcInsn extends ConstantInsn {
    private final LdcInsnNode node;

    public LdcInsn(Hierarchy hierarchy, LdcInsnNode node) {
        super(hierarchy, node);
        this.node = node;
        this.constant = this.node.cst instanceof Type type ? new TypeWrapper(type, hierarchy) : this.node.cst;
    }

    @Override
    public void resolve() {
        if (this.constant instanceof TypeWrapper type) {
            type.resolveHierarchy();
        }

        super.resolve();
    }

    @Override
    public AbstractInsnNode dump() {
        this.node.cst = this.constant instanceof TypeWrapper type ? type.dump() : this.constant;
        return super.dump();
    }

    @Override
    public String toString() {
        return "push(ldc) " + node.cst;
    }
}
