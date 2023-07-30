package dev.skidfuscator.ir.field.impl;

import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.signature.SignatureWrapper;
import dev.skidfuscator.ir.type.TypeWrapper;
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
    private TypeWrapper type;
    private SignatureWrapper signatureWrapper;
    private Object defaultValue;
    private List<FieldInvoker<?, ?>> invokers;
    private List<Annotation> annotations;
    private boolean resolvedHierachy;

    public ResolvedFieldNode(Hierarchy hierarchy, org.objectweb.asm.tree.FieldNode node) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.name = node.name;
        this.defaultValue = node.value;
        this.mutable = true;
        //System.out.println("Type: " + node.desc + " " + Type.getType(node.desc).getDescriptor());
        this.type = new TypeWrapper(Type.getType(node.desc), hierarchy);
        this.signatureWrapper = new SignatureWrapper(node.signature, hierarchy);

        this.invokers = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    @Override
    public void lock() {
        this.mutable = false;
    }

    @Override
    public void resolveHierachy() {
        this.type.resolveHierarchy();

        if (!this.signatureWrapper.isResolved()) {
            this.signatureWrapper.resolveHierarchy();
        }

        //System.out.printf("Resolved field %s with type %s --> %s\n",
        //        this.name, this.type.getOriginalType().getDescriptor(), this.type.getDesc());

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

        this.resolvedHierachy = true;
    }

    @Override
    public boolean isResolvedHierachy() {
        return resolvedHierachy;
    }

    @Override
    public org.objectweb.asm.tree.FieldNode dump() {
        if (!this.resolvedHierachy)
            throw new IllegalStateException(String.format(
                    "Field %s has not resolved hierarchy yet!", this.name
            ));

        this.node.name = name;
        this.node.desc = this.getType().getDescriptor();
        this.node.signature = this.getSignature();
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

        return this.node;
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
        return type.isResolved() ? type.dump() : type.getOriginalType();
    }

    @Override
    public String getSignature() {
        return signatureWrapper.isResolved() ? signatureWrapper.dump() : node.signature;
    }

    @Override
    public String getDesc() {
        return type.isResolved() ? type.dump().getDescriptor() : node.desc;
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
    public List<FieldInvoker<?, ?>> getInvokers() {
        return Collections.unmodifiableList(invokers);
    }

    @Override
    public void addInvoke(FieldInvoker<?, ?> invoker) {
        //System.out.println("Adding invoker " + invoker + " to " + this);
        this.invokers.add(invoker);
    }

    @Override
    public void removeInvoke(FieldInvoker<?, ?> invoker) {
        this.invokers.remove(invoker);
    }

    @Override
    public boolean isStatic() {
        return (this.node.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public String toString() {
        return type.isResolved()
                ? parent + "#" + name + type.dump().getDescriptor()
                : "UNRESOLVED " + parent + "#" + name + node.desc;
    }
}
