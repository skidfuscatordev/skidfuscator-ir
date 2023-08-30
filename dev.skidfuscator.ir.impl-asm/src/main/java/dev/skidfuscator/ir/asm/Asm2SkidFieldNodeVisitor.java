package dev.skidfuscator.ir.asm;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.asm.util.AsmUtil;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.*;

public class Asm2SkidFieldNodeVisitor extends FieldVisitor {
    private final Hierarchy hierarchy;
    private final Field visitor;

    public Asm2SkidFieldNodeVisitor(Hierarchy hierarchy, Field visitor) {
        super(AsmUtil.ASM_VERSION);

        this.hierarchy = hierarchy;
        this.visitor = visitor;
    }

    protected Asm2SkidFieldNodeVisitor(Hierarchy hierarchy, Field visitor, FieldVisitor fieldVisitor) {
        super(AsmUtil.ASM_VERSION, fieldVisitor);
        this.hierarchy = hierarchy;
        this.visitor = visitor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
