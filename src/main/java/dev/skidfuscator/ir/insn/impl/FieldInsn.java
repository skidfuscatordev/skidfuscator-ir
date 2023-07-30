package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.field.invoke.AbstractFieldInvoke;
import dev.skidfuscator.ir.field.invoke.InstructionFieldInvoke;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;

import java.util.stream.Collectors;

public class FieldInsn extends AbstractInsn<FieldInsnNode> {
    private FieldInvoker<?, ?> invoke;
    private boolean synthetic;

    public FieldInsn(Hierarchy hierarchy, FieldInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        this.invoke = new InstructionFieldInvoke(this);

        final KlassNode owner = hierarchy.resolveClass(node.owner);
        if (owner == null)
            throw new IllegalStateException(String.format(
                    "Could not find owner for %s.%s%s",
                    node.owner, node.name, node.desc
            ));

        try {
            this.setTarget(owner.getField(node.name, node.desc));
        } catch (IllegalStateException e) {
            throw new IllegalStateException(String.format(
                    "Could not find target for %s.%s%s, available feilds: \n%s",
                    node.owner, node.name, node.desc,
                    owner.getFields()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("\n"))
            ), e);
        }

        super.resolve();
    }

    @Override
    public FieldInsnNode dump() {
        this.node.owner = invoke.getTarget().getParent().getName();
        this.node.name = invoke.getTarget().getName();
        this.node.desc = invoke.getTarget().getDesc();

        return this.node;
    }

    public void setInvoker(FieldInvoker<?, ?> invoke) {
        final FieldInvoker<?, ?> old = this.invoke;
        final FieldNode target = old.getTarget();
        target.removeInvoke(old);

        this.invoke = invoke;
        this.invoke.setTarget(target);
    }

    public void setTarget(final FieldNode target) {
        this.invoke.setTarget(target);
    }

    public FieldNode getTarget() {
        this._checkResolve();
        return this.invoke.getTarget();
    }

    public String getName() {
        return invoke.getTarget().getName();
    }

    public String getType() {
        return invoke.getTarget().getType().getDescriptor();
    }

    public boolean isAssign() {
        return this.node.getOpcode() == Opcodes.PUTFIELD
                || this.node.getOpcode() == Opcodes.PUTSTATIC;
    }

    @Override
    public void setParent(InstructionList parent) {
        // Unique scenario: when instruction does not have
        // a parent, remove it from the invocation list
        // only re-add when the parent is set
        if (parent == null) {
            this.invoke.getTarget().removeInvoke(this.invoke);
            this.synthetic = true;
        } else if (this.synthetic) {
            //System.out.println("Adding invoke " + this.invoke.getTarget() + " to " + parent);
            this.invoke.getTarget().addInvoke(this.invoke);
        }

        super.setParent(parent);
    }

    public boolean isStatic() {
        return invoke.getTarget().isStatic();
    }

    @Override
    public String toString() {
        final boolean fetch = !isAssign();

        return fetch
                ? "push(field) " + invoke.getTarget()
                : invoke.getTarget() + " = pop()";
    }
}
