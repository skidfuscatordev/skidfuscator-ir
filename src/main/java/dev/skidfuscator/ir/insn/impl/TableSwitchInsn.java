package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.TableSwitchInsnNode;

public class TableSwitchInsn extends AbstractInsn {

    private final TableSwitchInsnNode node;

    public TableSwitchInsn(Hierarchy hierarchy, TableSwitchInsnNode node) {
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
