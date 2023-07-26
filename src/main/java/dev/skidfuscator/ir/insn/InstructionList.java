package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.method.FunctionNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InstructionList implements Iterable<Insn> {
    private final FunctionNode node;
    private final List<Insn> instructions;

    public InstructionList(FunctionNode node, List<Insn> instructions) {
        this.node = node;
        this.instructions = instructions;
    }

    public void add(final Insn insn) {
        insn.setParent(this);
        this.instructions.add(insn);
    }

    public void insert(final int index, final Insn insn) {
        insn.setParent(this);
        this.instructions.add(index, insn);
    }

    public void remove(final Insn insn) {
        insn.setParent(null);
        this.instructions.remove(insn);
    }

    public boolean isEmpty() {
        return this.instructions.isEmpty();
    }

    public List<Insn> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public FunctionNode getNode() {
        return node;
    }

    @NotNull
    @Override
    public Iterator<Insn> iterator() {
        return instructions.iterator();
    }
}
