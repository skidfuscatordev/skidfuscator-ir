package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;

import java.util.Collections;
import java.util.List;

public class UnresolvedKlassNode implements KlassNode {
    private final KlassNode root;
    private final String name;

    public UnresolvedKlassNode(KlassNode root, String name) {
        this.root = root;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void resolve() {

    }

    @Override
    public Type asType() {
        return Type.getObjectType("L" + this.getName() + ";");
    }

    @Override
    public KlassNode getParent() {
        return root;
    }

    @Override
    public void setParent(KlassNode klass) {
        throw new IllegalStateException(String.format(
                "Cannot set parent of unresolved class %s",
                name
        ));
    }

    @Override
    public List<KlassNode> getInterfaces() {
        return Collections.emptyList();
    }

    @Override
    public void setInterfaces(List<KlassNode> klasses) {
        throw new IllegalStateException(String.format(
                "Cannot set interfaces of unresolved class %s",
                name
        ));
    }

    @Override
    public List<FunctionNode> getMethods() {
        throw new IllegalStateException(String.format(
                "Cannot compute methods of unresolved class %s",
                name
        ));
    }

    @Override
    public void setMethods(List<FunctionNode> nodes) {
        throw new IllegalStateException(String.format(
                "Cannot set methods of unresolved class %s",
                name
        ));
    }

    @Override
    public void addMethod(FunctionNode node) {
        throw new IllegalStateException(String.format(
                "Cannot add method to unresolved class %s",
                name
        ));
    }

    @Override
    public void setName(String name) {
        throw new IllegalStateException(String.format(
                "Cannot set name of unresolved class %s",
                name
        ));
    }

    @Override
    public boolean isInterface() {
        throw new IllegalStateException(String.format(
                "Cannot compute access status of unresolved class %s",
                name
        ));
    }

    @Override
    public void dump() {
        throw new IllegalStateException(String.format(
                "Cannot dump unresolved class %s",
                name
        ));
    }
}