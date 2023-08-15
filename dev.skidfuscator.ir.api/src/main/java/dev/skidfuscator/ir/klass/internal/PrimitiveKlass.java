package dev.skidfuscator.ir.klass.internal;

public class PrimitiveKlass extends UnmodifiableKlass {
    public PrimitiveKlass(String name) {
        super(name);
    }

    @Override
    public String getDescriptor() {
        return super.getName();
    }
}
