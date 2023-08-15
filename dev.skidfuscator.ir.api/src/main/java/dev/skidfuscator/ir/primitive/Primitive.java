package dev.skidfuscator.ir.primitive;

import dev.skidfuscator.ir.klass.Klass;
import dev.skidfuscator.ir.klass.internal.PrimitiveArrayKlass;
import dev.skidfuscator.ir.klass.internal.PrimitiveKlass;

public enum Primitive {
    BOOLEAN(new PrimitiveKlass("Z")),
    BOOLEAN_ARRAY(new PrimitiveArrayKlass("[Z")),
    BYTE(new PrimitiveKlass("B")),
    BYTE_ARRAY(new PrimitiveArrayKlass("[B")),
    CHAR(new PrimitiveKlass("C")),
    CHAR_ARRAY(new PrimitiveArrayKlass("[C")),
    SHORT(new PrimitiveKlass("S")),
    SHORT_ARRAY(new PrimitiveArrayKlass("[S")),
    INT(new PrimitiveKlass("I")),
    INT_ARRAY(new PrimitiveArrayKlass("[I")),
    FLOAT(new PrimitiveKlass("F")),
    FLOAT_ARRAY(new PrimitiveArrayKlass("[F")),
    LONG(new PrimitiveKlass("J")),
    LONG_ARRAY(new PrimitiveArrayKlass("[J")),
    DOUBLE(new PrimitiveKlass("D")),
    DOUBLE_ARRAY(new PrimitiveArrayKlass("[D"))
    ;

    private final Klass klass;
    private final boolean array;

    Primitive(Klass klass) {
        this.klass = klass;
        this.array = klass.getName().contains("[");
    }

    public boolean isArray() {
        return array;
    }
}
