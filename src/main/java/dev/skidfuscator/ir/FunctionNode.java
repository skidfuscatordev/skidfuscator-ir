package dev.skidfuscator.ir;

import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionGroup;

import java.util.List;

public interface FunctionNode extends HierarchyResolvable {

    void resolve();

    void dump();

    List<Insn> getInstructions();

    KlassNode getParent();

    FunctionGroup getGroup();

    void setGroup(final FunctionGroup group);

    void setParent(final KlassNode node);

    String getName();

    void setName(final String name);

    String getDesc();

    boolean isStatic();

    boolean isConstructor();
}
