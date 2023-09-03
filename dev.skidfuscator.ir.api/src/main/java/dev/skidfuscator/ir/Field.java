package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.impl.FieldModifier;
import dev.skidfuscator.ir.field.FieldVisitor;

public non-sealed class Field extends FieldVisitor implements DirectFieldModifier {
    private Klass owner;
    private FieldModifier modifier;
    private String name;
    private Klass type;
    private Object constant;

    public Field() {
    }

    private Field(Klass owner, FieldModifier modifier, String name, Klass type, Object constant) {
        this.owner = owner;
        this.modifier = modifier;
        this.name = name;
        this.type = type;
        this.constant = constant;
    }

    public static Builder of() {
        return new Builder();
    }

    @Override
    public void visit(final Klass owner,
                      final FieldModifier modifier,
                      final String name,
                      final Klass type,
                      final Object constant) {
        this.owner = owner;
        this.modifier = modifier;
        this.name = name;
        this.type = type;
        this.constant = constant;

        super.visit(owner, modifier, name, type, constant);
    }

    @Override
    public FieldModifier getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(FieldModifier modifier) {
        this.modifier = modifier;
    }

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

    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    public static final class Builder {
        private Klass owner;
        private FieldModifier modifier;
        private String name;
        private Klass type;
        private Object constant;
        private FieldVisitor next;

        private Builder() {
        }

        public Builder owner(Klass owner) {
            this.owner = owner;
            return this;
        }

        public Builder modifier(FieldModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Klass type) {
            this.type = type;
            return this;
        }

        public Builder constant(Object constant) {
            this.constant = constant;
            return this;
        }

        public Builder but() {
            return of().owner(owner).modifier(modifier).name(name).type(type).constant(constant);
        }

        public Field build() {
            return new Field(owner, modifier, name, type, constant);
        }
    }
}
