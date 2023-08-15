package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.AbstractInstruction;
import dev.skidfuscator.ir.insn.IllegalInstructionException;
import dev.skidfuscator.ir.insn.InstructionVisitor;
import dev.skidfuscator.ir.primitive.Primitive;

public class ArithmeticInstruction extends AbstractInstruction {
    private Primitive type;
    private Operation operation;

    private ArithmeticInstruction(Primitive type, Operation operation) {
        this.type = type;
        this.operation = operation;
    }

    public Primitive getType() {
        return type;
    }

    public void setType(Primitive type) {
        this.type = type;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void visit(InstructionVisitor visitor) {
        visitor.visitArithmetic(type, operation);
    }

    public enum Operation {
        ADD(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE),
        SUB(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE),
        MUL(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE),
        DIV(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE),
        REM(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE),
        XOR(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.BOOLEAN),
        OR(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.BOOLEAN),
        AND(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.BOOLEAN),
        NEG(Primitive.BYTE, Primitive.CHAR, Primitive.SHORT, Primitive.INT, Primitive.FLOAT, Primitive.LONG, Primitive.DOUBLE);

        private final Primitive[] allowedTypes;

        Operation(Primitive... allowedTypes) {
            this.allowedTypes = allowedTypes;
        }

        public boolean isAllowed(final Primitive primitive) {
            for (Primitive allowedType : allowedTypes) {
                if (allowedType == primitive)
                    return true;
            }

            return false;
        }
    }

    public static ArithmeticInstructionBuilder of() {
        return new ArithmeticInstructionBuilder();
    }

    public static final class ArithmeticInstructionBuilder {
        private Primitive type;
        private Operation operation;

        private ArithmeticInstructionBuilder() {
        }

        public ArithmeticInstructionBuilder type(Primitive type) {
            this.type = type;
            return this;
        }

        public ArithmeticInstructionBuilder operation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public ArithmeticInstruction build() {
            if (type == null) {
                throw new IllegalInstructionException("Arithmetic instruction cannot have null type");
            }
            if (operation == null) {
                throw new IllegalInstructionException("Arimetic instruction cannot have null operation");
            }

            if (!operation.isAllowed(type)) {
                throw new IllegalInstructionException("Arithmetic operand %s does not allow %s", operation, type);
            }

            return new ArithmeticInstruction(type, operation);
        }
    }
}
