package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.method.dynamic.DynamicHandle;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

public class InvokeDynamicInsn extends AbstractInsn<InvokeDynamicInsnNode> {
    private String name;
    private String desc;
    private TypeWrapper descWrapper;
    private DynamicHandle bsm;
    private Object[] bsmArgs;
    public InvokeDynamicInsn(Hierarchy hierarchy, InvokeDynamicInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {
        final Handle handle = node.bsm;
        final FunctionNode target = hierarchy.findMethod(handle.getOwner(), handle.getName(), handle.getDesc());

        if (target == null) {
            throw new IllegalStateException(String.format(
                    "Failed to resolve dynamic handle %s#%s%s",
                    handle.getOwner(),
                    handle.getName(),
                    handle.getDesc()
            ));
        }

        this.bsm = new DynamicHandle(
                handle.getTag(),
                target
        );
        this.name = node.name;
        this.desc = node.desc;
        this.descWrapper = new TypeWrapper(Type.getMethodType(node.desc), hierarchy);
        this.descWrapper.resolveHierarchy();
        this.bsmArgs = node.bsmArgs;
        for (int i = 0; i < this.bsmArgs.length; i++) {
            final Object arg = this.bsmArgs[i];
            if (arg instanceof Type) {
                final TypeWrapper type = new TypeWrapper((Type) arg, hierarchy);
                type.resolveHierarchy();

                this.bsmArgs[i] = type;
            }

            else if (arg instanceof Handle) {
                final Handle h = (Handle) arg;
                final FunctionNode f = hierarchy.findMethod(h.getOwner(), h.getName(), h.getDesc());
                if (f == null) {
                    throw new IllegalStateException(String.format(
                            "Failed to resolve dynamic handle %s#%s%s",
                            h.getOwner(),
                            h.getName(),
                            h.getDesc()
                    ));
                }
                this.bsmArgs[i] = new DynamicHandle(h.getTag(), f);
            }
        }

        super.resolve();
    }

    @Override
    public InvokeDynamicInsnNode dump() {
        this.node.bsm = bsm.dump();
        this.node.name = name;
        this.node.desc = descWrapper.dump().getDescriptor();
        this.node.bsmArgs = new Object[this.bsmArgs.length];

        for (int i = 0; i < this.bsmArgs.length; i++) {
            final Object arg = this.bsmArgs[i];

            if (arg instanceof TypeWrapper) {
                this.node.bsmArgs[i] = ((TypeWrapper) arg).dump();
            } else if (arg instanceof DynamicHandle) {
                this.node.bsmArgs[i] = ((DynamicHandle) arg).dump();
            } else {
                this.node.bsmArgs[i] = arg;
            }
        }

        return super.dump();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.descWrapper.isResolved() ? descWrapper.dump().getDescriptor() : desc;
    }

    public TypeWrapper getDescWrapper() {
        return descWrapper;
    }

    public DynamicHandle getBsm() {
        return bsm;
    }

    public void setBsm(DynamicHandle bsm) {
        this.bsm = bsm;
    }

    public Object[] getBsmArgs() {
        return bsmArgs;
    }

    public void setBsmArgs(Object[] bsmArgs) {
        this.bsmArgs = bsmArgs;
    }

    @Override
    public String toString() {
        return "push(indy) " + node.name + node.desc;
    }
}
