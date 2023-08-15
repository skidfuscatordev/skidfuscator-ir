package dev.skidfuscator.ir.record;

import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.signature.SignatureWrapper;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.RecordComponentNode;

import java.util.ArrayList;
import java.util.List;

public class RecordComponent implements HierarchyResolvable {

    private final RecordComponentNode node;
    private final Hierarchy hierarchy;

    private String name;
    private final TypeWrapper descriptor;
    private final SignatureWrapper signature;
    private final List<Annotation> annotations;

    private final boolean strict;

    private boolean resolved;

    public RecordComponent(RecordComponentNode node, Hierarchy hierarchy, boolean strict) {
        this.node = node;
        this.hierarchy = hierarchy;

        this.name = node.name;
        this.descriptor = new TypeWrapper(Type.getType(node.descriptor), hierarchy);
        this.signature = new SignatureWrapper(node.signature, hierarchy);
        this.annotations = new ArrayList<>();
        this.strict = strict;
    }

    @Override
    public void resolveHierarchy() {
        if (resolved)
            throw new IllegalStateException(String.format(
                    "Record %s is already resolved",
                    name
            )); //IDK if this should be there

        this.resolved = true;
        if (!signature.isResolved())
            signature.resolveHierarchy();

        if (!descriptor.isResolved())
            descriptor.resolveHierarchy();

        if (node.visibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.VISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.visibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_VISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.invisibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.INVISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.invisibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_INVISIBLE);
                this.annotations.add(annotation);
            }
        }

        for (Annotation annotation : this.annotations) {
            try {
                annotation.resolve();
            } catch (IllegalStateException e) {
                if (strict) { //TODO
                    throw new IllegalStateException(
                            String.format("Failed to resolve annotation %s on %s", annotation.getNode().desc, this.name),
                            e
                    );
                } else {
                    System.err.println(
                            String.format("Failed to resolve annotation %s on %s", annotation.getNode().desc, this.name)
                    );
                    e.printStackTrace();
                }
            }
        }
    }

    public RecordComponentNode dump() {
        node.name = name;
        node.descriptor = descriptor.dump().getDescriptor();
        node.signature = signature.dump();

        for (Annotation annotation : annotations) {
            annotation.dump();
        }

        return node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isResolved() {
        return resolved;
    }
}
