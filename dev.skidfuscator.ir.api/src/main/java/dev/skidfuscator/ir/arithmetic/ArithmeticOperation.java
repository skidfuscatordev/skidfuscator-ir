package dev.skidfuscator.ir.arithmetic;

import dev.skidfuscator.ir.primitive.Primitive;

public enum ArithmeticOperation {
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

    ArithmeticOperation(Primitive... allowedTypes) {
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
