package dev.skidfuscator.ir.method;

public interface FunctionInvoker<T, P> {
    void setTarget(final FunctionNode node);

    FunctionNode getTarget();

    void replace(final P... insns);

    T get();

    Class<T> getType();


}
