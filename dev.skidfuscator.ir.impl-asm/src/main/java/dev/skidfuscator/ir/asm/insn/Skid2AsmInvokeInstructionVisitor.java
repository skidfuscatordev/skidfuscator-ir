package dev.skidfuscator.ir.asm.insn;

import dev.skidfuscator.ir.insn.impl.InvokeInstructionVisitor;
import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.Method;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Skid2AsmInvokeInstructionVisitor extends InvokeInstructionVisitor {
    private final MethodVisitor visitor;

    public Skid2AsmInvokeInstructionVisitor(MethodVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void copyFrom(Method target) {
        final int opcode;
        if (target.isStatic()) {
            opcode = Opcodes.INVOKESTATIC;
        } else if (target.isInit()) {
            opcode = Opcodes.INVOKESPECIAL;
        } else {
            opcode = target.getOwner().isInterface()
                    ? Opcodes.INVOKEINTERFACE
                    : Opcodes.INVOKEVIRTUAL;
        }

        visitor.visitMethodInsn(
                opcode,
                target.getOwner().getName(),
                target.getName(),
                toDesc(target),
                target.getOwner().isInterface()
        );

        super.copyFrom(target);
    }

    private String toDesc(final Method insn) {
        final StringBuilder builder = new StringBuilder("(");

        for (Klass arg : insn.getArgs()) {
            builder.append(arg.getDescriptor());
        }

        builder.append(")");
        builder.append(insn.getReturnType().getDescriptor());

        return builder.toString();
    }
}
