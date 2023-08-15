package dev.skidfuscator.ir;

public class Method {
    private boolean _static;
    private boolean _interface;
    private boolean _synthetic;
    private boolean _bridge;

    public boolean isStatic() {
        return _static;
    }

    public boolean isInterface() {
        return _interface;
    }

    public boolean isAnnotation() {
        return _synthetic;
    }


}
