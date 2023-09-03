package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.access.impl.ClassModifier;
import dev.skidfuscator.ir.field.FieldVisitor;
import dev.skidfuscator.ir.method.MethodVisitor;

import java.util.List;

public class KlassVisitor {
    private final KlassVisitor visitor;

    public KlassVisitor() {
        this(null);
    }

    public KlassVisitor(KlassVisitor visitor) {
        this.visitor = visitor;
    }

    public void visit(final String name, final Klass parent, final List<Klass> interfaces, final ClassModifier modifier, final String signature) {
        if (visitor != null) {
            visitor.visit(name, parent, interfaces, modifier, signature);
        }
    }

    public MethodVisitor visitMethod() {
        if (visitor != null) {
            return visitor.visitMethod();
        }

        return null;
    }

    public FieldVisitor visitField() {
        if (visitor != null) {
            return visitor.visitField();
        }

        return null;
    }
}
