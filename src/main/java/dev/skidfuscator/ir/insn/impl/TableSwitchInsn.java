package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

public class TableSwitchInsn extends AbstractInsn<TableSwitchInsnNode> {
    public TableSwitchInsn(Hierarchy hierarchy, TableSwitchInsnNode node) {
        super(hierarchy, node);
    }
}
