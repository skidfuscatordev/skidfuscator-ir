package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.method.MethodVisitor;

import java.util.List;
import java.util.Set;

public class KlassVisitor {
    private KlassVisitor visitor;

    public KlassVisitor() {
        this(null);
    }

    public KlassVisitor(KlassVisitor visitor) {
        this.visitor = visitor;
    }

    public void visit(final String name, final Klass parent, final List<Klass> interfaces, final Set<KlassTags> tags, final String signature) {
        if (visitor != null) {
            visitor.visit(name, parent, interfaces, tags, signature);
        }
    }

    public MethodVisitor visitMethod() {
        if (visitor != null) {
            return visitor.visitMethod();
        }

        return null;
    }
}
