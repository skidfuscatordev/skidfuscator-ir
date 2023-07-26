package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.method.FunctionInvoker;
import dev.skidfuscator.ir.method.invoke.StaticFunctionInvoke;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class InvokeInsn extends AbstractInsn {
    private final MethodInsnNode node;
    private FunctionInvoker<InvokeInsn> invoker;

    public InvokeInsn(Hierarchy hierarchy, MethodInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.invoker = new StaticFunctionInvoke(this);
        this.invoker.setTarget(hierarchy.findMethod(node));
    }

    @Override
    public AbstractInsnNode dump() {
        this.node.owner = invoker.getTarget().getParent().getName();
        this.node.name = invoker.getTarget().getName();
        this.node.desc = invoker.getTarget().getDesc();

        return node;
    }

    public FunctionNode getTarget() {
        return invoker.getTarget();
    }

    public void setTarget(final FunctionNode target) {
        this.invoker.setTarget(target);
    }

    public FunctionInvoker<InvokeInsn> getInvoker() {
        return invoker;
    }

    public boolean isStatic() {
        return invoker.get().isStatic();
    }
}
