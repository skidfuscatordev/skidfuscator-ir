package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.access.impl.MethodModifier;
import dev.skidfuscator.ir.insn.InstructionsVisitor;

import java.util.List;

public abstract class MethodVisitor {
    private MethodVisitor next;

    public MethodVisitor() {
    }

    public MethodVisitor(MethodVisitor next) {
        this.next = next;
    }

    public void visit(final Klass owner, final String name, final MethodModifier modifier, final List<Klass> args, final Klass returnType) {
        if (next != null) {
            next.visit(owner, name, modifier, args, returnType);
        }
    }

    public void visitGroup(final MethodGroup group) {
        if (next != null) {
            next.visitGroup(group);
        }
    }

    public InstructionsVisitor visitCode() {
        if (next != null) {
            return next.visitCode();
        }

        throw new IllegalStateException(
                "Trying to visit code in an undefined implementation"
        );
    }
}
