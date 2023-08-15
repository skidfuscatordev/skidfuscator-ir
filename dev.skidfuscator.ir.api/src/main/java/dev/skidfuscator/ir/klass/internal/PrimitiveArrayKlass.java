package dev.skidfuscator.ir.klass.internal;

public class PrimitiveArrayKlass extends UnmodifiableKlass {
    public PrimitiveArrayKlass(String name) {
        super(name);
    }

    @Override
    public String getDescriptor() {
        return super.getName();
    }
}
