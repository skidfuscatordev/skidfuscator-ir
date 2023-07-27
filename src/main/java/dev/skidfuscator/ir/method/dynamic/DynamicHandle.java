package dev.skidfuscator.ir.method.dynamic;

import dev.skidfuscator.ir.method.FunctionNode;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DynamicHandle {
    private final int tag;
    private final FunctionNode target;

    public DynamicHandle(int tag, FunctionNode target) {
        this.tag = tag;
        this.target = target;
    }

    public Handle dump() {
        return new Handle(
                tag,
                target.getOwner().getName(),
                target.getName(),
                target.getDesc(),
                target.getOwner().isInterface()
        );
    }

    public FunctionNode getTarget() {
        return target;
    }

    /**
     * Returns the kind of field or method designated by this handle.
     *
     * @return {@link Opcodes#H_GETFIELD}, {@link Opcodes#H_GETSTATIC}, {@link Opcodes#H_PUTFIELD},
     *     {@link Opcodes#H_PUTSTATIC}, {@link Opcodes#H_INVOKEVIRTUAL}, {@link
     *     Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL}, {@link
     *     Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
     */
    public int getTag() {
        return tag;
    }

    /**
     * Returns the internal name of the class that owns the field or method designated by this handle.
     *
     * @return the internal name of the class that owns the field or method designated by this handle
     *     (see {@link Type#getInternalName()}).
     */
    public String getOwner() {
        return target.getOwner().getName();
    }

    /**
     * Returns the name of the field or method designated by this handle.
     *
     * @return the name of the field or method designated by this handle.
     */
    public String getName() {
        return target.getName();
    }

    /**
     * Returns the descriptor of the field or method designated by this handle.
     *
     * @return the descriptor of the field or method designated by this handle.
     */
    public String getDesc() {
        return target.getDesc();
    }

    /**
     * Returns true if the owner of the field or method designated by this handle is an interface.
     *
     * @return true if the owner of the field or method designated by this handle is an interface.
     */
    public boolean isInterface() {
        return target.getOwner().isInterface();
    }
}
