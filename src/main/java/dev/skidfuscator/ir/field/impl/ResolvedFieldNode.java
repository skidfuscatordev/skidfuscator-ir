package dev.skidfuscator.ir.field.impl;

import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.TypeAnnotationNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResolvedFieldNode implements FieldNode {
    private final Hierarchy hierarchy;
    private final org.objectweb.asm.tree.FieldNode node;
    private boolean mutable;
    private String name;
    private KlassNode parent;
    private Type type;
    private Object defaultValue;
    private List<FieldInvoker<?>> invokers;
    private List<Annotation> annotations;

    public ResolvedFieldNode(Hierarchy hierarchy, org.objectweb.asm.tree.FieldNode node) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.name = node.name;
        this.defaultValue = node.value;
        this.mutable = true;
        this.type = Type.getObjectType(node.desc);
        this.invokers = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    @Override
    public void lock() {
        this.mutable = false;
    }

    @Override
    public void resolve() {
        if (node.visibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.VISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }

        if (node.visibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_VISIBLE);
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
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_INVISIBLE);
                annotation.resolve();

                this.annotations.add(annotation);
            }
        }
    }

    @Override
    public void dump() {
        this.node.name = name;
        this.node.desc = type.getInternalName();
        this.node.value = defaultValue;

        this.node.visibleAnnotations = null;
        this.node.visibleTypeAnnotations = null;
        this.node.invisibleAnnotations = null;
        this.node.invisibleTypeAnnotations = null;

        if (!this.annotations.isEmpty()) {
            for (Annotation annotation : this.annotations) {
                switch (annotation.getType()) {
                    case VISIBLE:
                        if (this.node.visibleAnnotations == null)
                            this.node.visibleAnnotations = new ArrayList<>();

                        this.node.visibleAnnotations.add(annotation.getNode());
                        break;
                    case INVISIBLE:
                        if (this.node.invisibleAnnotations == null)
                            this.node.invisibleAnnotations = new ArrayList<>();

                        this.node.invisibleAnnotations.add(annotation.getNode());
                        break;
                    case TYPE_VISIBLE:
                        if (this.node.visibleTypeAnnotations == null)
                            this.node.visibleTypeAnnotations = new ArrayList<>();

                        this.node.visibleTypeAnnotations.add((TypeAnnotationNode) annotation.getNode());
                        break;
                    case TYPE_INVISIBLE:
                        if (this.node.invisibleTypeAnnotations == null)
                            this.node.invisibleTypeAnnotations = new ArrayList<>();
                        this.node.invisibleTypeAnnotations.add((TypeAnnotationNode) annotation.getNode());
                        break;
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify locked field");

        this.name = name;
    }

    @Override
    public KlassNode getParent() {
        return parent;
    }

    public void setParent(KlassNode parent) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify locked field");

        this.parent = parent;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public void setDefault(final Object obj) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify locked field");

        this.defaultValue = obj;
    }

    @Override
    public List<FieldInvoker<?>> getInvokers() {
        return Collections.unmodifiableList(invokers);
    }

    @Override
    public void addInvoke(FieldInvoker<?> invoker) {
        //System.out.println("Adding invoker " + invoker + " to " + this);
        this.invokers.add(invoker);
    }

    @Override
    public void removeInvoke(FieldInvoker<?> invoker) {
        this.invokers.remove(invoker);
    }

    @Override
    public boolean isStatic() {
        return (this.node.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public String toString() {
        return parent + "#" + name + type.getInternalName() + " = " + defaultValue;
    }
}
