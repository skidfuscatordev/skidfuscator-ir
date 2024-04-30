package dev.skidfuscator.ir.primitive;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.klass.internal.PrimitiveArrayKlass;
import dev.skidfuscator.ir.klass.internal.PrimitiveKlass;

public enum Primitive {
    VOID(PrimitiveKlass.VOID),
    BOOLEAN(PrimitiveKlass.BOOLEAN),
    BOOLEAN_ARRAY(new PrimitiveArrayKlass("[Z")),
    BYTE(PrimitiveKlass.BYTE),
    BYTE_ARRAY(new PrimitiveArrayKlass("[B")),
    CHAR(PrimitiveKlass.CHAR),
    CHAR_ARRAY(new PrimitiveArrayKlass("[C")),
    SHORT(PrimitiveKlass.SHORT),
    SHORT_ARRAY(new PrimitiveArrayKlass("[S")),
    INT(PrimitiveKlass.INT),
    INT_ARRAY(new PrimitiveArrayKlass("[I")),
    FLOAT(PrimitiveKlass.FLOAT),
    FLOAT_ARRAY(new PrimitiveArrayKlass("[F")),
    LONG(PrimitiveKlass.LONG),
    LONG_ARRAY(new PrimitiveArrayKlass("[J")),
    DOUBLE(PrimitiveKlass.DOUBLE),
    DOUBLE_ARRAY(new PrimitiveArrayKlass("[D"));

    private final Klass klass;
    private final boolean array;

    Primitive(Klass klass) {
        this.klass = klass;
        this.array = klass.getName().startsWith("[");
    }

    public Klass getKlass() {
        return klass;
    }

    public boolean isArray() {
        return array;
    }
    
    public boolean isDouble() {
        return this == DOUBLE || this == DOUBLE_ARRAY;
    }
    
    public boolean isLong() {
        return this == LONG || this == LONG_ARRAY;
    }
    
    public boolean isInt() {
        return this == INT || this == INT_ARRAY;
    }
    
    public boolean isFloat() {
        return this == FLOAT || this == FLOAT_ARRAY;
    }
    
    public boolean isShort() {
        return this == SHORT || this == SHORT_ARRAY;
    }
    
    public boolean isChar() {
        return this == CHAR || this == CHAR_ARRAY;
    }
    
    public boolean isByte() {
        return this == BYTE || this == BYTE_ARRAY;
    }
    
    public boolean isBoolean() {
        return this == BOOLEAN || this == BOOLEAN_ARRAY;
    }
    
    public boolean isInteger() {
        return isInt();
    }
    
    public boolean isDoubleStack() {
        return isDouble() || isLong();
    }
}
