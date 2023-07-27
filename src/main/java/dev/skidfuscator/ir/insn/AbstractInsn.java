package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsn<T extends AbstractInsnNode> implements Insn<T> {
    protected final Hierarchy hierarchy;
    protected T node;
    private boolean resolved;
    private InstructionList parent;

    public AbstractInsn(Hierarchy hierarchy, T node) {
        this.hierarchy = hierarchy;
        this.node = node;
    }

    public T node() {
        return node;
    }

    @Override
    public T dump() {
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
        this.parent = parent;
    }

    @Override
    public void replace(Insn... node) {
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
