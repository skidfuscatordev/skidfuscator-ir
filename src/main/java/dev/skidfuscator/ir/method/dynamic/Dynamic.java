package dev.skidfuscator.ir.method.dynamic;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

public class Dynamic<T> implements HierarchyResolvable {

    private final T node;
    private final Hierarchy hierarchy;

    private String name;
    private String desc;
    private TypeWrapper descWrapper;
    private Handle handle;
    private DynamicHandle bsm;
    private Object[] bsmArgs;

    public Dynamic(T dynamic, Hierarchy hierarchy) {
        this.node = dynamic;
        this.hierarchy = hierarchy;

        if (dynamic instanceof InvokeDynamicInsnNode invokeDynamic) {
            this.name = invokeDynamic.name;
            this.desc = invokeDynamic.desc;
            this.descWrapper = new TypeWrapper(Type.getType(desc), hierarchy);
            this.bsmArgs = invokeDynamic.bsmArgs;
            this.handle = invokeDynamic.bsm;
        } else if (dynamic instanceof ConstantDynamic constantDynamic) {
            this.name = constantDynamic.getName();
            this.desc = constantDynamic.getDescriptor();
            this.descWrapper = new TypeWrapper(Type.getType(desc), hierarchy);
            this.handle = constantDynamic.getBootstrapMethod();
            this.bsmArgs = new Object[constantDynamic.getBootstrapMethodArgumentCount()];
            for (int i = 0; i < this.bsmArgs.length; i++) {
                this.bsmArgs[i] = constantDynamic.getBootstrapMethodArgument(i);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void resolveHierarchy() {
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

        if (!descWrapper.isResolved())
            descWrapper.resolveHierarchy();

        for (int i = 0; i < this.bsmArgs.length; i++) {
            final Object arg = this.bsmArgs[i];
            if (arg instanceof Type type) {
                final TypeWrapper typeWrapper = new TypeWrapper(type, hierarchy);
                typeWrapper.resolveHierarchy();

                this.bsmArgs[i] = type;
            } else if (arg instanceof Handle argHandle) {
                final FunctionNode functionNode = hierarchy.findMethod(argHandle.getOwner(), argHandle.getName(), argHandle.getDesc());
                if (functionNode == null) {
                    throw new IllegalStateException(String.format(
                            "Failed to resolve dynamic handle %s#%s%s",
                            argHandle.getOwner(),
                            argHandle.getName(),
                            argHandle.getDesc()
                    ));
                }
                this.bsmArgs[i] = new DynamicHandle(argHandle.getTag(), functionNode);
            } else if (arg instanceof ConstantDynamic constantDynamic) {
                final Dynamic<ConstantDynamic> dynamic = new Dynamic<>(constantDynamic, hierarchy);
                dynamic.resolveHierarchy();
                this.bsmArgs[i] = dynamic;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T dump() {
        Object[] args = new Object[this.bsmArgs.length];
        for (int i = 0; i < this.bsmArgs.length; i++) {
            final Object arg = this.bsmArgs[i];

            if (arg instanceof TypeWrapper typeWrapper) {
                args[i] = typeWrapper.dump();
            } else if (arg instanceof DynamicHandle dynamicHandle) {
                args[i] = dynamicHandle.dump();
            } else if (arg instanceof Dynamic<?> dynamic && dynamic.node instanceof ConstantDynamic) {
                args[i] = dynamic.dump();
            } else {
                args[i] = arg;
            }
        }

        if (node instanceof InvokeDynamicInsnNode invokeDynamic) {
            invokeDynamic.name = name;
            invokeDynamic.desc = descWrapper.dump().getDescriptor();
            invokeDynamic.bsmArgs = args;
            invokeDynamic.bsm = bsm.dump();
            return node;
        } else if (node instanceof ConstantDynamic) {
            return (T) new ConstantDynamic(
                    name,
                    descWrapper.dump().getDescriptor(),
                    bsm.dump(),
                    args
            );
        } else {
            throw new IllegalArgumentException();
        }
    }

    public T getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TypeWrapper getDescWrapper() {
        return descWrapper;
    }

    public void setDescWrapper(TypeWrapper descWrapper) {
        this.descWrapper = descWrapper;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
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
}
