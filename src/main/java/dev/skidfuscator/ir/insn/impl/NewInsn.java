package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TypeInsnNode;

public class NewInsn extends AbstractInsn {
    private final TypeInsnNode node;
    private KlassNode target;

    public NewInsn(Hierarchy hierarchy, TypeInsnNode node) {
        super(hierarchy);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.target = hierarchy.findClass(Type.getObjectType(node.desc).getClassName().replace(".", "/"));
    }

    @Override
    public void dump() {
        this.node.desc = "L" + target.getName() + ";";
    }
}