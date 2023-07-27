package dev.skidfuscator.ir.field.invoke;

import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.FieldInsn;
import org.jetbrains.annotations.NotNull;

public class StaticFieldInvoke implements FieldInvoker<FieldInsn> {
    private final FieldInsn insn;
    private FieldNode target;

    public StaticFieldInvoke(@NotNull final FieldInsn insn) {
        assert insn != null : "Insn cannot be null!";

        this.insn = insn;
    }

    @Override
    public void setTarget(@NotNull final FieldNode target) {
        assert target != null : String.format(
                "Target cannot be null! (Aimed for %s#%s%s)",
                insn
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

        System.out.println(String.format("Setting target for %s to %s", insn, target));
        target.addInvoke(this);
    }

    @Override
    @NotNull
    public FieldNode getTarget() {
        assert target != null : "Critical! Self target is null!";

        return target;
    }

    @Override
    public boolean isAssign() {
        return insn.isAssign();
    }

    @Override
    public void replace(Insn... insns) {
        insn.replace(insns);
    }

    @Override
    public FieldInsn get() {
        return insn;
    }

    @Override
    public Class<FieldInsn> getType() {
        return FieldInsn.class;
    }

    @Override
    public String toString() {
        return insn.toString();
    }
}
