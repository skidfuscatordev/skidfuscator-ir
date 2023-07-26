package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.FunctionNode;

public interface FieldInvoker<T> {
    void setTarget(final FieldNode node);

    FieldNode getTarget();

    T get();

    Class<T> getType();
}
