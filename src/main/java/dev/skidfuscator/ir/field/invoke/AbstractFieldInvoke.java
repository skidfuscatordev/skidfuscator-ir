package dev.skidfuscator.ir.field.invoke;

import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.FieldInsn;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFieldInvoke<T, P> implements FieldInvoker<T, P> {
    private FieldNode target;
    protected final T caller;

    public AbstractFieldInvoke(T insn) {
        this.caller = insn;
    }

    @Override
    public void setTarget(@NotNull final FieldNode target) {
        assert target != null : String.format(
                "Target cannot be null! (Aimed for %s)",
                caller
        );

        // Force proper removal
        if (this.target != null) {
            final FieldNode old = this.target;
            this.target = null;
            old.removeInvoke(this);
        }

        // Proper assignment, order is important
        // must be:
        // 1. set this.target
        // 2. add self to invoke list of target
        this.target = target;

        //System.out.println(String.format("Setting target for %s to %s", insn, target));
        target.addInvoke(this);
    }

    @Override
    @NotNull
    public FieldNode getTarget() {
        assert target != null : "Critical! Self target is null!";

        return target;
    }

    @Override
    public abstract boolean isAssign();

    @Override
    public abstract void replace(P... insns);

    @Override
    public T get() {
        return caller;
    }

    @Override
    public abstract Class<T> getType();

    @Override
    public String toString() {
        return caller.toString();
    }
}
