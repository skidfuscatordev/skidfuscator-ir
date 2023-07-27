package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.impl.LabelInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeAnnotationNode;

import java.util.ArrayList;
import java.util.List;

public class TryCatchBlock {
    private final Hierarchy hierarchy;
    private final InstructionList list;
    private final TryCatchBlockNode node;
    private LabelInsn start;
    private LabelInsn end;
    private LabelInsn handler;
    @Nullable
    private KlassNode type;
    private List<Annotation> annotations;

    public TryCatchBlock(Hierarchy hierarchy, InstructionList list, TryCatchBlockNode node) {
        this.hierarchy = hierarchy;
        this.list = list;
        this.node = node;
    }

    public void resolve() {
        this.start = list.getLabel(node.start);
        this.end = list.getLabel(node.end);
        this.handler = list.getLabel(node.handler);
        this.type = this.node.type == null
            ? null
            : this.hierarchy.findClass(this.node.type);

        if (node.visibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_VISIBLE);
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

    public TryCatchBlockNode dump() {
        this.node.start = this.start.dump();
        this.node.end = this.end.dump();
        this.node.handler = this.handler.dump();
        this.node.type = this.type == null ? null : this.type.getName();

        if (!this.annotations.isEmpty()) {
            for (Annotation annotation : this.annotations) {
                switch (annotation.getType()) {
                    case VISIBLE:
                    case INVISIBLE:
                        throw new UnsupportedOperationException("Cannot dump visible or invisible annotations on try catch blocks");
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

        return node;
    }

    public LabelInsn getStart() {
        return start;
    }

    public void setStart(LabelInsn start) {
        this.start = start;
    }

    public LabelInsn getEnd() {
        return end;
    }

    public void setEnd(LabelInsn end) {
        this.end = end;
    }

    public LabelInsn getHandler() {
        return handler;
    }

    public void setHandler(LabelInsn handler) {
        this.handler = handler;
    }

    public KlassNode getType() {
        return type;
    }

    public void setType(KlassNode type) {
        this.type = type;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
