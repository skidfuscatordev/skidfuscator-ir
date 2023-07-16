package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import org.objectweb.asm.Type;

import java.util.List;

public interface KlassNode {
    void resolve();

    Type asType();

    String getName();

    void setName(final String name);

    KlassNode getParent();

    void setParent(final KlassNode klass);

    List<KlassNode> getInterfaces();

    void setInterfaces(final List<KlassNode> klasses);

    List<FunctionNode> getMethods();

    void setMethods(final List<FunctionNode> nodes);

    void addMethod(final FunctionNode node);

    boolean isInterface();

    void dump();
}
