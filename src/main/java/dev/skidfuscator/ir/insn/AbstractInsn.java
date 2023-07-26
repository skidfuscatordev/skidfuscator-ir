package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class AbstractInsn implements Insn {
    protected final Hierarchy hierarchy;
    protected AbstractInsnNode node;
    private boolean resolved;
    private InstructionList parent;

    public AbstractInsn(Hierarchy hierarchy, AbstractInsnNode node) {
        this.hierarchy = hierarchy;
        this.node = node;
    }

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

    protected void _checkResolve() {
        assert resolved : String.format(
                "Instruction in %s of type %s needs to be resolved!",
                parent.getNode(),
                node
        );
    }
}
