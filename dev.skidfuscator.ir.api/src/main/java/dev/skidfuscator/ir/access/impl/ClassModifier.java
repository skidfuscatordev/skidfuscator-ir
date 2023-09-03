package dev.skidfuscator.ir.access.impl;

import dev.skidfuscator.ir.access.Access;
import dev.skidfuscator.ir.access.Modifier;
import dev.skidfuscator.ir.access.Target;

public class ClassModifier extends Modifier {
    private ClassModifier(int access) {
        super(Target.CLASS, access);
    }

    public static Builder of() {
        return new Builder();
    }

    public static ClassModifier of(int access) {
        return new ClassModifier(access);
    }

    public static ClassModifier of(Access... accesses) {
        ClassModifier modifier = new ClassModifier(0);
        modifier.set(accesses);
        return modifier;
    }

    public static ClassModifier empty() {
        return of(0);
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

    public boolean isFinal() {
        return is(Access.FINAL);
    }

    public boolean isSuper() {
        return is(Access.SUPER);
    }

    public boolean isInterface() {
        return is(Access.INTERFACE);
    }

    public boolean isAbstract() {
        return is(Access.ABSTRACT);
    }

    public boolean isSynthetic() {
        return is(Access.SYNTHETIC);
    }

    public boolean isAnnotation() {
        return is(Access.ANNOTATION);
    }

    public boolean isEnum() {
        return is(Access.ENUM);
    }

    public boolean isMandated() {
        return is(Access.MANDATED);
    }

    public boolean isModule() {
        return is(Access.MODULE);
    }

    public boolean isRecord() {
        return is(Access.RECORD);
    }

    public boolean isDeprecated() {
        return is(Access.DEPRECATED);
    }

    public static final class Builder {

        private final ClassModifier modifier = new ClassModifier(0);

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

        public Builder _final() {
            modifier.set(Access.FINAL);
            return this;
        }

        public Builder _super() {
            modifier.set(Access.SUPER);
            return this;
        }

        public Builder _interface() {
            modifier.set(Access.INTERFACE);
            return this;
        }

        public Builder _abstract() {
            modifier.set(Access.ABSTRACT);
            return this;
        }

        public Builder synthetic() {
            modifier.set(Access.SYNTHETIC);
            return this;
        }

        public Builder annotation() {
            modifier.set(Access.INTERFACE, Access.ANNOTATION);
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

        public Builder module() {
            modifier.set(Access.MODULE);
            return this;
        }

        public Builder record() {
            modifier.set(Access.RECORD);
            return this;
        }

        public Builder deprecated() {
            modifier.set(Access.DEPRECATED);
            return this;
        }

        public ClassModifier build() {
            return modifier;
        }
    }
}
