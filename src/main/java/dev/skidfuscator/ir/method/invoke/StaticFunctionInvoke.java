package dev.skidfuscator.ir.method.invoke;

import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.insn.impl.InvokeInsn;
import dev.skidfuscator.ir.method.FunctionInvoker;
import org.jetbrains.annotations.NotNull;

public class StaticFunctionInvoke implements FunctionInvoker<InvokeInsn> {
    private final InvokeInsn insn;
    private FunctionNode target;

    public StaticFunctionInvoke(@NotNull final InvokeInsn insn) {
        assert insn != null : "Insn cannot be null!";

        this.insn = insn;
    }

    @Override
    public void setTarget(@NotNull final FunctionNode target) {
        assert target != null : "Target cannot be null!";

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
        target.addInvoke(this);
    }

    @Override
    @NotNull
    public FunctionNode getTarget() {
        assert target != null : "Critical! Self target is null!";

        return target;
    }

    @Override
    public InvokeInsn get() {
        return insn;
    }

    @Override
    public Class<InvokeInsn> getType() {
        return InvokeInsn.class;
    }
}
