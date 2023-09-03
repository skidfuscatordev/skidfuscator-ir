package dev.skidfuscator.ir.annotation;

public class AnnotationVisitor {
    private AnnotationVisitor next;

    public AnnotationVisitor() {
    }

    public AnnotationVisitor(AnnotationVisitor next) {
        this.next = next;
    }

    //public void visit(final ) //TODO
}
