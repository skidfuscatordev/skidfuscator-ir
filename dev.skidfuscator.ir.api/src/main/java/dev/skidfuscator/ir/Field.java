package dev.skidfuscator.ir;

public class Field {
    private Klass owner;
    private String name;
    private Klass type;
    private boolean _static;

    public Klass getOwner() {
        return owner;
    }

    public void setOwner(Klass owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Klass getType() {
        return type;
    }

    public void setType(Klass type) {
        this.type = type;
    }

    public boolean isStatic() {
        return _static;
    }
}
