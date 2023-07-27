package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.List;
import java.util.stream.Collectors;

public class TableSwitchInsn extends AbstractInsn<TableSwitchInsnNode> {
    private int min;
    private int max;
    private LabelInsn dflt;
    private List<LabelInsn> labels;

    public TableSwitchInsn(Hierarchy hierarchy, TableSwitchInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        this.min = node.min;
        this.max = node.max;
        this.dflt = this.getParent().getLabel(node.dflt);
        this.labels = node.labels.stream().map(e -> this.getParent().getLabel(e)).collect(Collectors.toList());

        super.resolve();
    }

    @Override
    public TableSwitchInsnNode dump() {
        this.node.min = this.min;
        this.node.max = this.max;
        this.node.dflt = this.dflt.dump();
        this.node.labels = this.labels.stream().map(AbstractInsn::dump).collect(Collectors.toList());

        return super.dump();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public LabelInsn getDflt() {
        return dflt;
    }

    public void setDflt(LabelInsn dflt) {
        this.dflt = dflt;
    }

    public List<LabelInsn> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelInsn> labels) {
        this.labels = labels;
    }
}
