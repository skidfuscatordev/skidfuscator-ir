package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.LineNumberNode;

public class LineNumberInsn extends AbstractInsn<LineNumberNode> {
    public LineNumberInsn(Hierarchy hierarchy, LineNumberNode node) {
        super(hierarchy, node);
    }
}
