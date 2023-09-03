package dev.skidfuscator.ir.access.impl;

import dev.skidfuscator.ir.access.Access;
import dev.skidfuscator.ir.access.Modifier;
import dev.skidfuscator.ir.access.Target;

public class FieldModifier extends Modifier {

    private FieldModifier(int access) {
        super(Target.FIELD, access);
    }

    public static Builder of() {
        return new Builder();
    }

    public static FieldModifier of(int access) {
        return new FieldModifier(access);
    }

    public static FieldModifier of(Access... accesses) {
        FieldModifier modifier = new FieldModifier(0);
        modifier.set(accesses);
        return modifier;
    }

    public static FieldModifier empty() {
        return of(0);
    }

    public void setStatic() {
        set(Access.STATIC);
    }

    public void setNotStatic() {
        remove(Access.STATIC);
    }

    public boolean isPublic() {
        return is(Access.PUBLIC);
    }

    public boolean isPrivate() {
        return is(Access.PRIVATE);
    }

    public boolean isProtected() {
        return is(Access.PROTECTED);
    }

    public boolean isStatic() {
        return is(Access.STATIC);
    }

    public boolean isFinal() {
        return is(Access.FINAL);
    }

    public boolean isVolatile() {
        return is(Access.VOLATILE);
    }

    public boolean isTransient() {
        return is(Access.TRANSIENT);
    }

    public boolean isSynthetic() {
        return is(Access.SYNTHETIC);
    }

    //TODO: Im not sure what FIELD_INNER means in asm xd
    public boolean isEnum() {
        return is(Access.ENUM);
    }

    public boolean isMandated() {
        return is(Access.MANDATED);
    }

    public boolean isDeprecated() {
        return is(Access.DEPRECATED);
    }

    public static final class Builder {

        private final FieldModifier modifier = new FieldModifier(0);

        private Builder() {
        }

        public Builder _public() {
            modifier.setPublic();
            return this;
        }

        public Builder _private() {
            modifier.setPrivate();
            return this;
        }

        public Builder _protected() {
            modifier.setProtected();
            return this;
        }

        public Builder _static() {
            modifier.set(Access.STATIC);
            return this;
        }

        public Builder _final() {
            modifier.set(Access.FINAL);
            return this;
        }

        public Builder _volatile() {
            modifier.set(Access.VOLATILE);
            return this;
        }

        public Builder _transient() {
            modifier.set(Access.TRANSIENT);
            return this;
        }

        public Builder synthetic() {
            modifier.set(Access.SYNTHETIC);
            return this;
        }

        public Builder _enum() {
            modifier.set(Access.ENUM);
            return this;
        }

        public Builder mandated() {
            modifier.set(Access.MANDATED);
            return this;
        }

        public Builder deprecated() {
            modifier.set(Access.DEPRECATED);
            return this;
        }

        public FieldModifier build() {
            return modifier;
        }
    }
}
