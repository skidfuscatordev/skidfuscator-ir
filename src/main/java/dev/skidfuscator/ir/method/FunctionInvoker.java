package dev.skidfuscator.ir.method;

public interface FunctionInvoker<T> {
    void setTarget(final FunctionNode node);

    FunctionNode getTarget();

    T get();

    Class<T> getType();
}
