package dev.skidfuscator.ir;

import dev.skidfuscator.ir.field.FieldTags;
import dev.skidfuscator.ir.field.FieldVisitor;

import java.util.Set;

public class Field extends FieldVisitor {
    private Klass owner;
    private Set<FieldTags> tags;
    private String name;
    private Klass type;

    private Object dflt;
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

    public Object getDefault() {
        return dflt;
    }

    public void setDefault(Object dflt) {
        this.dflt = dflt;
    }

    public boolean isStatic() {
        return _static;
    }

    @Override
    public void visit(final Klass owner,
                      final Set<FieldTags> tags,
                      final String name,
                      final Klass type,
                      final Object dflt) {
        this.owner = owner;
        this.tags = tags;
        this.name = name;
        this.type = type;
        this.dflt = dflt;

        super.visit(owner, tags, name, type, dflt);
    }

    public static FieldBuilder of() {
        return new FieldBuilder();
    }

    public static final class FieldBuilder {
        private Klass owner;
        private Set<FieldTags> tags;
        private String name;
        private Klass type;
        private Object dflt;
        private FieldVisitor next;

        private FieldBuilder() {
        }

        public FieldBuilder owner(Klass owner) {
            this.owner = owner;
            return this;
        }

        public FieldBuilder tags(Set<FieldTags> tags) {
            this.tags = tags;
            return this;
        }

        public FieldBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FieldBuilder type(Klass type) {
            this.type = type;
            return this;
        }

        public FieldBuilder dflt(Object dflt) {
            this.dflt = dflt;
            return this;
        }

        public FieldBuilder but() {
            return of().owner(owner).tags(tags).name(name).type(type).dflt(dflt);
        }

        public Field build() {
            Field field = new Field();
            field.setOwner(owner);
            field.setName(name);
            field.setType(type);
            field.dflt = this.dflt;
            field.tags = this.tags;
            return field;
        }
    }
}
