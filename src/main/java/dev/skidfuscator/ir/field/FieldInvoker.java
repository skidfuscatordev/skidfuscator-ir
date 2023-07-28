package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.insn.Insn;

public interface FieldInvoker<T, P> {
    void setTarget(final FieldNode node);

    FieldNode getTarget();

    boolean isAssign();

    void replace(final P... insns);

    T get();

    Class<T> getType();
}
