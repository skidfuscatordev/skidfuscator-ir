package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.Klass;

import java.util.Set;

public class FieldVisitor {
    private FieldVisitor next;

    public FieldVisitor() {
    }

    public FieldVisitor(FieldVisitor next) {
        this.next = next;
    }

    public void visit(final Klass owner, final Set<FieldTags> tags, final String name, final Klass type, final Object dflt) {
        if (next != null)
            next.visit(owner, tags, name, type, dflt);
    }
}
