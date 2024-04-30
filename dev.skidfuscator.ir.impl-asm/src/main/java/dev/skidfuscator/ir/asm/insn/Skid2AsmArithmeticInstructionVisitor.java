package dev.skidfuscator.ir.asm.insn;

import dev.skidfuscator.ir.arithmetic.ArithmeticOperation;
import dev.skidfuscator.ir.insn.impl.visitor.ArithmeticInstructionVisitor;
import dev.skidfuscator.ir.primitive.Primitive;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Skid2AsmArithmeticInstructionVisitor extends ArithmeticInstructionVisitor {
    private final MethodVisitor visitor;

    public Skid2AsmArithmeticInstructionVisitor(MethodVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void copyFrom(Primitive type, ArithmeticOperation operation) {
        switch (operation) {
            case ADD -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LADD);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FADD);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DADD);}
                    default -> {visitor.visitInsn(Opcodes.IADD);}
                }
            }
            case SUB -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LSUB);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FSUB);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DSUB);}
                    default -> {visitor.visitInsn(Opcodes.ISUB);}
                }
            }
            case MUL -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LMUL);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FMUL);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DMUL);}
                    default -> {visitor.visitInsn(Opcodes.IMUL);}
                }
            }
            case DIV -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LDIV);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FDIV);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DDIV);}
                    default -> {visitor.visitInsn(Opcodes.IDIV);}
                }
            }
            case REM -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LREM);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FREM);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DREM);}
                    default -> {visitor.visitInsn(Opcodes.IREM);}
                }
            }
            case SHL -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LSHL);}
                    default -> {visitor.visitInsn(Opcodes.ISHL);}
                }
            }
            case SHR -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LSHR);}
                    default -> {visitor.visitInsn(Opcodes.ISHR);}
                }
            }
            case USHR -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LUSHR);}
                    default -> {visitor.visitInsn(Opcodes.IUSHR);}
                }
            }
            case NEG -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LNEG);}
                    case FLOAT -> {visitor.visitInsn(Opcodes.FNEG);}
                    case DOUBLE -> {visitor.visitInsn(Opcodes.DNEG);}
                    default -> {visitor.visitInsn(Opcodes.INEG);}
                }
            }
            case OR -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LOR);}
                    default -> {visitor.visitInsn(Opcodes.IOR);}
                }
            }
            case AND -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LAND);}
                    default -> {visitor.visitInsn(Opcodes.IAND);}
                }
            }
            case XOR -> {
                switch (type) {
                    case LONG -> {visitor.visitInsn(Opcodes.LXOR);}
                    default -> {visitor.visitInsn(Opcodes.IXOR);}
                }
            }
        }
    }
}
