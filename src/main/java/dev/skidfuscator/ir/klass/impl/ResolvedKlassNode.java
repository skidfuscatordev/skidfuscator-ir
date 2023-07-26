package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
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
    private List<Annotation> annotations;
    private String name;
    private int access;
    private List<FunctionNode> methods;
    private List<FieldNode> fields;

    public ResolvedKlassNode(Hierarchy hierarchy, ClassNode node) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.implementations = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.name = node.name;
        this.access = node.access;
    }

    @Override
    public void resolveHierarchy() {
        if (node.superName != null) {
            this.parent = hierarchy.findClass(node.superName);
        }

        if (node.interfaces != null) {
            for (String interfaze : node.interfaces) {
                final KlassNode resolved = hierarchy.findClass(interfaze);
                this.implementations.add(resolved);
            }
        }

        hierarchy.resolveKlassEdges(this);
    }

    @Override
    public void resolveInternal() {
        /*
         * Since the classes are being resolved in
         * a BFS manner, the method groups will be
         * fine.
         */
        for (FunctionNode method : this.methods) {
            method.resolve();
        }

        if (node.visibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.VISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }

        if (node.visibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.VISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }

        if (node.invisibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.INVISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }

        if (node.invisibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.INVISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }

        hierarchy.resolveKlassEdges(this);
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
    public void setMethods(List<FunctionNode> nodes) {
        for (FunctionNode method : this.getMethods()) {
            method.setParent(null);
        }

        this.methods = nodes;
    }

    @Override
    public void addMethod(FunctionNode node) {
        node.setParent(this);

        this.methods.add(node);
    }

    @Override
    public void removeMethod(FunctionNode node) {

    }

    @Override
    public @NotNull List<FieldNode> getFields() {
        return null;
    }

    @Override
    public void addField(FieldNode node) {

    }

    @Override
    public void removeField(FieldNode node) {

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

        for (FunctionNode method : methods) {
            method.dump();
        }

        
    }

    @Override
    public String toString() {
        return name;
    }
}
