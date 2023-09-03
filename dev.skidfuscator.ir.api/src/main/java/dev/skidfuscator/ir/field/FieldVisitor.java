package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.access.impl.FieldModifier;

public class FieldVisitor {
    private FieldVisitor next;

    public FieldVisitor() {
    }

    public FieldVisitor(FieldVisitor next) {
        this.next = next;
    }

    public void visit(final Klass owner, final FieldModifier modifier, final String name, final Klass type, final Object constant) {
        if (next != null)
            next.visit(owner, modifier, name, type, constant);
    }
}
