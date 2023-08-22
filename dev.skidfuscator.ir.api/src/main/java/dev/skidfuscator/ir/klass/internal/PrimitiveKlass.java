package dev.skidfuscator.ir.klass.internal;

public class PrimitiveKlass extends UnmodifiableKlass {
    public static final PrimitiveKlass VOID = new PrimitiveKlass("V");
    public static final PrimitiveKlass BOOLEAN = new PrimitiveKlass("Z");
    public static final PrimitiveKlass BYTE = new PrimitiveKlass("B");
    public static final PrimitiveKlass CHAR = new PrimitiveKlass("C");
    public static final PrimitiveKlass SHORT = new PrimitiveKlass("S");
    public static final PrimitiveKlass INT = new PrimitiveKlass("I");
    public static final PrimitiveKlass FLOAT = new PrimitiveKlass("F");
    public static final PrimitiveKlass LONG = new PrimitiveKlass("J");
    public static final PrimitiveKlass DOUBLE = new PrimitiveKlass("D");

    public PrimitiveKlass(String name) {
        super(name);
    }

    @Override
    public String getDescriptor() {
        return super.getName();
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }
}
