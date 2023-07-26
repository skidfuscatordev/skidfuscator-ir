package dev.skidfuscator.ir.field;

public interface FieldInvoker<T> {
    void setTarget(final FieldNode node);

    FieldNode getTarget();

    T get();

    Class<T> getType();
}
