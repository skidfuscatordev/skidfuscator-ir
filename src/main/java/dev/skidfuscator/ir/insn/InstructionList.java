package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.insn.impl.LabelInsn;
import dev.skidfuscator.ir.method.FunctionNode;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.LabelNode;

import java.util.*;

public class InstructionList implements Iterable<Insn<?>> {
    private final FunctionNode node;
    private final List<Insn<?>> instructions;
    private final Map<LabelNode, LabelInsn> labels;

    public InstructionList(FunctionNode node, List<Insn<?>> instructions) {
        this.node = node;
        this.instructions = instructions;
        this.labels = new HashMap<>();
    }

    public LabelInsn getLabel(final LabelNode node) {
        return labels.get(node);
    }

    public Insn<?> getFirst() {
        assert !this.instructions.isEmpty() : "Instruction list is empty";

        return this.instructions.get(0);
    }

    public Insn<?> get(final int index) {
        return this.instructions.get(index);
    }


    public void add(final Insn<?> insn) {
        insn.setParent(this);
        this.instructions.add(insn);

        if (insn instanceof LabelInsn) {
            this.labels.put(((LabelInsn) insn).node(), (LabelInsn) insn);
        }
    }

    public void insertBefore(final Insn<?> old, final Insn<?> newInsn) {
        this.insert(this.instructions.indexOf(old), newInsn);
    }

    public void insertAfter(final Insn<?> old, final Insn<?> newInsn) {
        this.insert(this.instructions.indexOf(old) + 1, newInsn);
    }
    @Deprecated
    public void insert(final Insn<?> old, final Insn<?> newInsn) {
        this.insertAfter(old, newInsn);
    }

    public void insert(final int index, final Insn<?> insn) {
        insn.setParent(this);
        this.instructions.add(index, insn);

        if (insn instanceof LabelInsn) {
            this.labels.put(((LabelInsn) insn).node(), (LabelInsn) insn);
        }
    }

    public void remove(final Insn<?> insn) {
        insn.setParent(null);
        this.instructions.remove(insn);

        if (insn instanceof LabelInsn) {
            this.labels.remove(((LabelInsn) insn).node());
        }
    }

    public int indexOf(final Insn<?> insn) {
        return this.instructions.indexOf(insn);
    }

    public int size() {
        return this.instructions.size();
    }

    public boolean isEmpty() {
        return this.instructions.isEmpty();
    }

    public List<Insn<?>> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public FunctionNode getNode() {
        return node;
    }

    @NotNull
    @Override
    public Iterator<Insn<?>> iterator() {
        return instructions.iterator();
    }
}
