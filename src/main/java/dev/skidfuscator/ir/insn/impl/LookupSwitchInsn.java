package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class LookupSwitchInsn extends AbstractInsn<LookupSwitchInsnNode> {
    private List<Integer> keys;
    private List<LabelInsn> labels;
    private LabelInsn dflt;
    public LookupSwitchInsn(Hierarchy hierarchy, LookupSwitchInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        this.keys = new ArrayList<>(this.node.keys);
        this.labels = new ArrayList<>(
                this.node.labels.stream().map(e -> this.getParent().getLabel(e)).collect(Collectors.toList())
        );
        this.dflt = this.getParent().getLabel(this.node.dflt);
    }

    @Override
    public LookupSwitchInsnNode dump() {
        /*this.node.labels.clear();
        this.node.keys.clear();
        this.node.keys.addAll(this.keys);
        this.node.labels.addAll(this.labels.stream().map(LabelInsn::dump).collect(Collectors.toList()));
        this.node.dflt = this.dflt.dump();*/

        return node;
    }


    public List<Integer> getKeys() {
        return keys;
    }

    public List<LabelInsn> getLabels() {
        return labels;
    }

    public LabelInsn getDflt() {
        return dflt;
    }
}
