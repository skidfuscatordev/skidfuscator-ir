package dev.skidfuscator.ir.field.invoke;

import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.FieldInsn;
import org.jetbrains.annotations.NotNull;

public class InstructionFieldInvoke extends AbstractFieldInvoke<FieldInsn, Insn<?>> {

    public InstructionFieldInvoke(@NotNull final FieldInsn insn) {
        super(insn);
        assert insn != null : "Insn cannot be null!";
    }

    @Override
    public boolean isAssign() {
        return caller.isAssign();
    }

    @Override
    public void replace(Insn<?>... insns)  {
        caller.replace(insns);
    }

    @Override
    public Class<FieldInsn> getType() {
        return FieldInsn.class;
    }
}
