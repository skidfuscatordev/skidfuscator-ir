package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.LabelNode;

public class LabelInsn extends AbstractInsn<LabelNode> {
    public LabelInsn(Hierarchy hierarchy, LabelNode node) {
        super(hierarchy, node);
    }
}
