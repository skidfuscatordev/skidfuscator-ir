package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
//import dev.skidfuscator.ir.gen.BytecodeFrontend;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.method.FunctionInvoker;
import dev.skidfuscator.ir.method.invoke.StaticFunctionInvoke;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvokeInsn extends AbstractInsn {
    private final MethodInsnNode node;
    private FunctionInvoker<InvokeInsn> invoker;
    private boolean synthetic;

    public InvokeInsn(Hierarchy hierarchy, MethodInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.invoker = new StaticFunctionInvoke(this);
        final FunctionNode target = hierarchy.findMethod(node);

        if (target == null) {
            final KlassNode owner = hierarchy.findClass(node.owner);

            if (owner == null)
                throw new IllegalStateException(String.format(
                        "Could not find owner for %s.%s%s",
                        node.owner, node.name, node.desc
                ));

            throw new IllegalStateException(String.format(
                    "Could not find target for %s.%s%s, available methods: \n%s",
                    node.owner, node.name, node.desc,
                    owner.getMethods()
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("\n"))
            ));
        }

        this.setTarget(target);

        super.resolve();
    }

    @Override
    public AbstractInsnNode dump() {
        this.node.owner = invoker.getTarget().getOwner().getName();
        this.node.name = invoker.getTarget().getName();
        this.node.desc = invoker.getTarget().getDesc();

        return node;
    }

    @Override
    public void setParent(InstructionList parent) {
        // Unique scenario: when instruction does not have
        // a parent, remove it from the invocation list
        // only re-add when the parent is set
        if (parent == null) {
            this.synthetic = true;
            this.invoker.getTarget().removeInvoke(this.invoker);
        } else if (this.synthetic) {
            this.synthetic = false;
            this.invoker.getTarget().addInvoke(this.invoker);
        }

        super.setParent(parent);
    }


    public FunctionNode getTarget() {
        _checkResolve();
        return invoker.getTarget();
    }

    public void setTarget(final FunctionNode target) {
        this.invoker.setTarget(target);
    }

    public FunctionInvoker<InvokeInsn> getInvoker() {
        _checkResolve();
        return invoker;
    }

    public boolean isStatic() {
        _checkResolve();
        return invoker.get().isStatic();
    }

    @Override
    public String toString() {
        return "push(invoke) " + getTarget();
    }
}
