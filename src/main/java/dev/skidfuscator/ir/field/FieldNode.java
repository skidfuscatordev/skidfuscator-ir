package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;

import java.util.List;

public interface FieldNode {
    KlassNode getParent();

    void setParent(final KlassNode node);

    void resolve();

    void dump();

    Type getType();

    Object getDefault();

    void setDefault(final Object obj);

    List<FieldInvoker<?>> getInvokers();

    void addInvoker(final FieldInvoker<?> invoker);

    void removeInvoker(final FieldInvoker<?> invoker);
}
