package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.FunctionNode;

public interface FunctionInvoker<T> {
    void setTarget(final FunctionNode node);

    FunctionNode getTarget();

    T get();

    Class<T> getType();
}
