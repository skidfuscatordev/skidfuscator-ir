package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.IllegalInstructionException;
import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.primitive.Primitive;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

public class ArithmeticInstruction extends ArithmeticInstructionVisitor implements Instruction {
    private Primitive type;
    private Operation operation;

    protected ArithmeticInstruction() {
    }

    protected ArithmeticInstruction(ArithmeticInstructionVisitor parent) {
        super(parent);
    }

    public @NotNull Primitive getType() {
        assertNotNull(type);
        return type;
    }

    public void setType(Primitive type) {
        Assert.nonNull(type, new IllegalInstructionException(
                "Arithmetic instruction cannot have null type"
        ));
        this.type = type;
    }

    public @NotNull Operation getOperation() {
        assertNotNull(type);
        return operation;
    }

    public void setOperation(Operation operation) {
        Assert.nonNull(operation, new IllegalInstructionException(
                "Arithmetic instruction cannot have null operation"
        ));
        Assert.isTrue(operation.isAllowed(type), new IllegalInstructionException(
                "Arithmetic operand %s does not allow %s", operation, type)
        );

        this.operation = operation;
    }

    /**
     * Basic visitor pattern renamed to help with beginners
     * without a CS degree. This is synonymous to visit.
     *
     * @param visitor Visitor being visited
     */
    @Override
    public void copyTo(AbstractInstructionsVisitor visitor) {
        copyTo(visitor.visitArithmetic());
    }

    public void copyTo(ArithmeticInstructionVisitor visitor) {
        visitor.copyFrom(type, operation);
    }

    @Override
    public void copyFrom(Primitive type, Operation operation) {
        this.setType(type);
        this.setOperation(operation);

        super.copyFrom(type, operation);
    }

    public void copyFrom(ArithmeticInstruction visitor) {
        visitor.copyTo(this);
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
            final ArithmeticInstruction instruction = new ArithmeticInstruction();
            instruction.copyFrom(type, operation);

            return instruction;
        }
    }
}
