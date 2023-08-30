package dev.skidfuscator.ir.method.invoke;

import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.insn.impl.InvokeInsn;
import dev.skidfuscator.ir.method.FunctionInvoker;
import org.jetbrains.annotations.NotNull;

public class StaticFunctionInvoke extends AbstractFunctionInvoke<InvokeInsn, Insn<?>> {
    public StaticFunctionInvoke(InvokeInsn insn) {
        super(insn);
    }

    @Override
    public void replace(Insn<?>... insns) {
        caller.replace(insns);
    }

    @Override
    public Class<InvokeInsn> getType() {
        return InvokeInsn.class;
    }
}
