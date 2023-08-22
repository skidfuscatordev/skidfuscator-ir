package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.insn.impl.AbstractInstructionsVisitor;
import dev.skidfuscator.ir.klass.Klass;

import java.util.List;
import java.util.Set;

public abstract class MethodVisitor {
    private MethodVisitor next;

    public MethodVisitor() {
    }

    public MethodVisitor(MethodVisitor next) {
        this.next = next;
    }

    public void visit(final Klass owner, final String name, final Set<MethodTags> tags, final List<Klass> args, final Klass returnType) {
        if (next != null) {
            next.visit(owner, name, tags, args, returnType);
        }
    }

    public void visitGroup(final MethodGroup group) {
        if (next != null) {
            next.visitGroup(group);
        }
    }

    public AbstractInstructionsVisitor visitCode() {
        if (next != null) {
            return next.visitCode();
        }

        throw new IllegalStateException(
                "Trying to visit code in an undefined implementation"
        );
    }
}
