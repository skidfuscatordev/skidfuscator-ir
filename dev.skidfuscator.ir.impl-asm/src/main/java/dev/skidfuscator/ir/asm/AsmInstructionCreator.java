package dev.skidfuscator.ir.asm;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.InstructionVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmInstructionCreator extends InstructionVisitor {
    private final MethodVisitor visitor;

    public AsmInstructionCreator(MethodVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void visitSetField(Field target) {
        final int opcode = target.isStatic() ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD;
        this.visitField(opcode, target);

        super.visitSetField(target);
    }

    @Override
    public void visitGetField(Field target) {
        final int opcode = target.isStatic() ? Opcodes.GETSTATIC : Opcodes.GETFIELD;
        this.visitField(opcode, target);

        super.visitGetField(target);
    }

    private void visitField(final int opcode, final Field target) {
        final String owner = target.getOwner().getName();
        final String name = target.getName();
        final String desc = target.getType().getDescriptor();

        visitor.visitFieldInsn(
                opcode,
                owner,
                name,
                desc
        );
    }

    @Override
    public void visitIinc(int local, int increment) {
        final int opcode = 0;
        visitor.visitIincInsn(local, increment);

        super.visitIinc(local, increment);
    }

    @Override
    public void visitInvoke(Method target) {
        super.visitInvoke(target);
    }

    @Override
    public void visitLabel(int offset) {
        super.visitLabel(offset);
    }

    @Override
    public void visitConstant(Object constant) {
        if (constant instanceof Number number) {
            if (constant instanceof Byte || constant instanceof Short || constant instanceof Integer) {
                final int value = number.intValue();
                this.visitInt(value);
            } else if (constant instanceof Float value) {
                if (value == 0F || value == 1F || value == 2F) {
                    visitor.visitInsn(Opcodes.FCONST_0 + value.intValue());
                } else {
                    visitor.visitLdcInsn(value);
                }
            } else if (constant instanceof Long value) {
                if (value == 0L || value == 1L) {
                    visitor.visitInsn(Opcodes.LCONST_0 + value.intValue());
                } else {
                    visitor.visitLdcInsn(value);
                }
            } else if (constant instanceof Double value) {
                if (value == 0D || value == 1D) {
                    visitor.visitInsn(Opcodes.DCONST_0 + value.intValue());
                } else {
                    visitor.visitLdcInsn(value);
                }
            }
        } else if (constant instanceof Character character)  {
            this.visitInt(character);
        } else if (constant == null) {
            visitor.visitInsn(Opcodes.ACONST_NULL);
        } else {
            visitor.visitLdcInsn(constant);
        }

        super.visitConstant(constant);
    }

    private void visitInt(final int value) {
        switch (value) {
            case -1, 0, 1, 2, 3, 4, 5 -> visitor.visitInsn(Opcodes.ICONST_0 + value);
            default -> {
                if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE)
                    visitor.visitIntInsn(Opcodes.BIPUSH, value);
                else if (Short.MIN_VALUE <= value && value <= Short.MAX_VALUE)
                    visitor.visitIntInsn(Opcodes.SIPUSH, value);
                else
                    visitor.visitLdcInsn(value);
            }
        }
    }
}
