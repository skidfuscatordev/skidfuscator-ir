package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

public class FrameInsn extends AbstractInsn<FrameNode> {
    public FrameInsn(Hierarchy hierarchy, FrameNode node) {
        super(hierarchy, node);
    }
}
