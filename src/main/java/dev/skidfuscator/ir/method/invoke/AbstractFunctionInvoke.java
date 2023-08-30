package dev.skidfuscator.ir.method.invoke;

import dev.skidfuscator.ir.method.FunctionInvoker;
import dev.skidfuscator.ir.method.FunctionNode;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFunctionInvoke<T, P> implements FunctionInvoker<T, P> {
    private FunctionNode target;
    protected final T caller;

    public AbstractFunctionInvoke(T insn) {
        this.caller = insn;
    }

    @Override
    public void setTarget(@NotNull final FunctionNode target) {
        assert target != null : String.format(
                "Target cannot be null! (Aimed for %s)",
                caller
        );

        // Force proper removal
        if (this.target != null) {
            final FunctionNode old = this.target;
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
    public FunctionNode getTarget() {
        assert target != null : "Critical! Self target is null!";

        return target;
    }

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
