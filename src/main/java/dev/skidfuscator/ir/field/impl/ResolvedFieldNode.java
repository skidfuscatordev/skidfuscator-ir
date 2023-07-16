package dev.skidfuscator.ir.field.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionGroup;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Stack;

public class ResolvedFieldNode implements FieldNode {

    private final org.objectweb.asm.tree.FieldNode node;

    private KlassNode parent;
    private String name;

    private String desc;

    private int access;

    public ResolvedFieldNode(org.objectweb.asm.tree.FieldNode node) {
        this.node = node;
    }

    @Override
    public void resolve() {
        this.access = node.access;
        this.name = node.name;
        this.desc = node.desc;
    }

    @Override
    public void dump() {

    }

    @Override
    public KlassNode getParent() {
        return this.parent;
    }

    @Override
    public void setParent(KlassNode node) {
        this.parent = node;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public boolean isStatic() {
        return (this.access & Opcodes.ACC_STATIC) != 0;
    }
}
