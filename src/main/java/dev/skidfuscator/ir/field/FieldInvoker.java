package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.insn.Insn;

public interface FieldInvoker<T> {
    void setTarget(final FieldNode node);

    FieldNode getTarget();

    boolean isAssign();

    void replace(final Insn... insns);

    T get();

    Class<T> getType();
}
