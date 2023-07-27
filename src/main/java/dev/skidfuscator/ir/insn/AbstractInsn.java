package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsn<T extends AbstractInsnNode> implements Insn<T> {
    protected final Hierarchy hierarchy;
    protected T node;
    private boolean mutable;
    private boolean resolved;
    private InstructionList parent;

    public AbstractInsn(Hierarchy hierarchy, T node) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.mutable = true;
    }

    public T node() {
        return node;
    }

    @Override
    public void lock() {
        this.mutable = false;
    }

    @Override
    public T dump() {
        if (!mutable) {
            throw new IllegalStateException("Cannot dump immutable instruction!");
        }

        return node;
    }

    @Override
    public int getOpcode() {
        return node.getOpcode();
    }

    @Override
    public void resolve() {
        this.resolved = true;
    }

    public InstructionList getParent() {
        return parent;
    }

    public void setParent(InstructionList parent) {
        if (!mutable) {
            throw new IllegalStateException("Cannot set parent of immutable instruction!");
        }

        this.parent = parent;
    }

    @Override
    public void replace(Insn<?>... node) {
        if (!mutable) {
            throw new IllegalStateException("Cannot replace immutable instruction!");
        }

        final int index = this.getParent().indexOf(this);
        this.getParent().remove(this);

        for (int i = node.length - 1; i >= 0; i--) {
            this.getParent().insert(index, node[i]);
        }
    }

    protected void _checkResolve() {
        assert resolved : String.format(
                "Instruction in %s of type %s needs to be resolved!",
                parent.getNode(),
                node
        );
    }
}
