package dev.skidfuscator.ir.api;

import dev.skidfuscator.ir.JavaMethod;

// Mock class to represent a Method
public class MockMethod extends JavaMethod {
    private boolean isStatic;

    public MockMethod(boolean isStatic) {
        this.isStatic = isStatic;
    }

    @Override
    public boolean isStatic() {
        return this.isStatic;
    }
}