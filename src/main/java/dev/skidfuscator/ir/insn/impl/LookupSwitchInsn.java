package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

public class LookupSwitchInsn extends AbstractInsn {

    private final LookupSwitchInsnNode node;

    public LookupSwitchInsn(Hierarchy hierarchy, LookupSwitchInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }


    @Override
    public void resolve() {

    }

    @Override
    public void dump() {

    }
}
