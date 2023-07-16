package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResolvedKlassNode implements KlassNode {
    private final Hierarchy hierarchy;
    private final ClassNode node;

    private KlassNode parent;
    private List<KlassNode> implementations;
    private String name;
    private int access;

    private List<FunctionNode> methods;

    private List<FieldNode> fields;

    public ResolvedKlassNode(Hierarchy hierarchy, ClassNode node) {
        this.hierarchy = hierarchy;
        this.node = node;
    }

    @Override
    public void resolve() {
        if (node.superName != null) {
            this.parent = hierarchy.findClass(node.superName);
        }

        if (node.interfaces != null) {
            this.implementations = new ArrayList<>();
            for (String interfaze : node.interfaces) {
                final KlassNode resolved = hierarchy.findClass(interfaze);
                this.implementations.add(resolved);
            }
        }

        if (node.methods != null) {
            this.methods = new ArrayList<>();

            for (MethodNode method : node.methods) {
                final FunctionNode node = hierarchy.findMethod(
                        this.node.name,
                        method.name,
                        method.desc
                );

                this.methods.add(node);
            }

            /*
             * Since the classes are being resolved in
             * a BFS manner, the method groups will be
             * fine.
             */
            for (FunctionNode method : this.methods) {
                method.resolve();
            }
        }

        if (node.fields != null) {
            this.fields = new ArrayList<>();

            for (org.objectweb.asm.tree.FieldNode field : node.fields) {
                final FieldNode node = hierarchy.findField(
                        this.node.name,
                        field.name,
                        field.desc
                );

                this.fields.add(node);
            }

            for (FieldNode field : this.fields) {
                field.resolve();
            }
        }
    }

    @Override
    public @NotNull Type asType() {
        return Type.getObjectType("L" + this.getName() + ";");
    }

    @Override
    public @NotNull List<FunctionNode> getMethods() {
        return this.methods == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(methods);
    }

    @Override
    public @NotNull List<FieldNode> getFields() {
        return this.fields == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(fields);
    }

    @Override
    public void setMethods(List<FunctionNode> nodes) {
        for (FunctionNode method : this.getMethods()) {
            method.setParent(null);
        }

        this.methods = nodes;
    }

    @Override
    public void setFields(@Nullable List<FieldNode> nodes) {
        for (FieldNode method : this.getFields()) {
            method.setParent(null);
        }

        this.fields = nodes;
    }

    @Override
    public void addMethod(FunctionNode node) {
        node.setParent(this);

        this.methods.add(node);
    }

    @Override
    public void addField(FieldNode node) {
        node.setParent(this);

        this.fields.add(node);
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public void setName(final @NotNull String name) {
        this.name = name;
    }

    @Override
    public KlassNode getParent() {
        return parent;
    }

    @Override
    public void setParent(KlassNode klass) {
        this.parent = klass;

        // Update hierarchy
        this.hierarchy.resolveKlassEdges(this);
    }

    @Override
    public @NotNull List<KlassNode> getInterfaces() {
        return implementations == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(implementations);
    }

    @Override
    public void setInterfaces(List<KlassNode> klasses) {
        this.implementations = klasses;

        // Update hierarchy
        this.hierarchy.resolveKlassEdges(this);
    }

    @Override
    public boolean isInterface() {
        return (this.access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public void dump() {
        this.node.name = name;
        this.node.access = access;
    }
}
