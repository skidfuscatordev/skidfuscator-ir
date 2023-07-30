package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.field.impl.ResolvedFieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.impl.ResolvedImmutableFunctionNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.*;

public class UnresolvedKlassNode implements KlassNode {
    private final Hierarchy hierarchy;
    private final KlassNode root;
    private final String name;

    private final Map<Descriptor, FunctionNode> ghostMethods;
    private final Map<Descriptor, FieldNode> ghostFields;

    public UnresolvedKlassNode(final Hierarchy hierarchy, final KlassNode root, String name) {
        this.hierarchy = hierarchy;
        this.root = root;
        this.name = name;
        this.ghostMethods = new HashMap<>();
        this.ghostFields = new HashMap<>();
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
    public void lock() {

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
    public @NotNull Collection<FunctionNode> getMethods() {
        return root.getMethods();
    }

    @Override
    public @NotNull FunctionNode getMethod(String name, String desc) {
        FunctionNode node = root.getMethod(name, desc);

        if (hierarchy.getConfig().isGeneratePhantomMethods() && node == null) {
            final Descriptor descriptor = new Descriptor(name, desc);
            node = ghostMethods.get(descriptor);

            if (node != null)
                return node;

            node = new ResolvedImmutableFunctionNode(
                    hierarchy,
                    descriptor,
                    null
            );
            node.setOwner(this);
            this.hierarchy.addMethod(node);
            this.ghostMethods.put(descriptor, node);

            return node;
        }

        if (node == null) {
            throw new IllegalStateException(String.format(
                    "Cannot get method of unresolved class %s",
                    name
            ));
        }

        return node;
    }

    @Override
    public @NotNull FieldNode getField(String name, String desc) {
        FieldNode node = null;
        try {
            node = root.getField(name, desc);
        } catch (IllegalStateException e) {
            if (hierarchy.getConfig().isGeneratePhantomFields()) {
                final Descriptor descriptor = new Descriptor(name, desc);
                node = ghostFields.get(descriptor);

                if (node != null)
                    return node;

                node = new ResolvedFieldNode(
                        hierarchy,
                        new org.objectweb.asm.tree.FieldNode(
                                Opcodes.ACC_PUBLIC,
                                name,
                                desc,
                                null,
                                null
                        )
                );
                this.ghostFields.put(descriptor, node);

                return node;
            } else {
                throw new IllegalStateException(String.format(
                        "Cannot get field of unresolved class %s",
                        name
                ), e);
            }
        }

        return node;
    }

    @Override
    public @NotNull List<FieldNode> getFields() {
        throw new IllegalStateException(String.format(
                "Cannot compute fields of unresolved class %s",
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
    public String getSignature() {
        throw new UnsupportedOperationException();
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
