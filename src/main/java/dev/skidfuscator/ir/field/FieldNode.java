package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;

import java.util.List;

public interface FieldNode {
    KlassNode getParent();

    void lock();

    String getName();

    String getDesc();

    void setName(final String name);

    void setParent(final KlassNode node);

    void resolveHierachy();

    org.objectweb.asm.tree.FieldNode dump();

    Type getType();

    Object getDefault();

    void setDefault(final Object obj);

    boolean isStatic();

    List<FieldInvoker<?, ?>> getInvokers();

    void addInvoke(final FieldInvoker<?, ?> invoker);

    void removeInvoke(final FieldInvoker<?, ?> invoker);
}
