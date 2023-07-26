package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.klass.KlassNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean isResolvedHierarchy() {
        return true;
    }

    @Override
    public boolean isResolvedInternal() {
        return true;
    }

    @Override
    public void resolveHierarchy() {

    }

    @Override
    public void resolveInternal() {

    }

    @Override
    public void resolveInstructions() {

    }

    @Override
    public @NotNull Type asType() {
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
    public @NotNull List<KlassNode> getInterfaces() {
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
    public @NotNull List<FunctionNode> getMethods() {
        throw new IllegalStateException(String.format(
                "Cannot compute methods of unresolved class %s",
                name
        ));
    }

    @Override
    public @NotNull FunctionNode getMethod(String name, String desc) {
        throw new IllegalStateException(String.format(
                "Cannot get method of unresolved class %s",
                name
        ));
    }

    @Override
    public @NotNull List<FieldNode> getFields() {
        throw new IllegalStateException(String.format(
                "Cannot compute fields of unresolved class %s",
                name
        ));
    }

    @Override
    public void setFields(@Nullable List<FieldNode> nodes) {
        throw new IllegalStateException(String.format(
                "Cannot set fields of unresolved class %s",
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
    public void addField(FieldNode node) {
        throw new IllegalStateException(String.format(
                "Cannot add field to unresolved class %s",
                name
        ));
    }

    @Override
    public void setName(@NotNull String name) {
        throw new IllegalStateException(String.format(
                "Cannot set name of unresolved class %s",
                name
        ));
    }

    @Override
    public void removeMethod(FunctionNode node) {
        throw new IllegalStateException(String.format(
                "Cannot remove method of unresolved class %s",
                name
        ));
    }

    @Override
    public void removeField(FieldNode node) {
        throw new IllegalStateException(String.format(
                "Cannot remove field of unresolved class %s",
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

    @Override
    public String toString() {
        return "UNRESOLVED/" + name;
    }
}
