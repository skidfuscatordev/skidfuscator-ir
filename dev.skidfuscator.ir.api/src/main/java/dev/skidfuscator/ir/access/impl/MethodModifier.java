package dev.skidfuscator.ir.access.impl;

import dev.skidfuscator.ir.access.Access;
import dev.skidfuscator.ir.access.Modifier;
import dev.skidfuscator.ir.access.Target;

public class MethodModifier extends Modifier {
    private MethodModifier(int access) {
        super(Target.METHOD, access);
    }

    public static Builder of() {
        return new Builder();
    }

    public static MethodModifier of(int access) {
        return new MethodModifier(access);
    }

    public static MethodModifier of(Access... accesses) {
        MethodModifier modifier = new MethodModifier(0);
        modifier.set(accesses);
        return modifier;
    }

    public static MethodModifier empty() {
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

    public boolean isSynchronized() {
        return is(Access.PUBLIC);
    }

    public boolean isBridge() {
        return is(Access.BRIDGE);
    }

    public boolean isVarArgs() {
        return is(Access.VARARGS);
    }

    public boolean isNative() {
        return is(Access.NATIVE);
    }

    public boolean isAbstract() {
        return is(Access.ABSTRACT);
    }

    public boolean isStrict() {
        return is(Access.STRICT);
    }

    public boolean isSynthetic() {
        return is(Access.SYNTHETIC);
    }

    public boolean isMandated() {
        return is(Access.MANDATED);
    }

    public boolean isDeprecated() {
        return is(Access.DEPRECATED);
    }

    public static final class Builder {

        private final MethodModifier modifier = new MethodModifier(0);

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

        public Builder _synchronized() {
            modifier.set(Access.SYNCHRONIZED);
            return this;
        }

        public Builder bridge() {
            modifier.set(Access.BRIDGE);
            return this;
        }

        public Builder varArgs() {
            modifier.set(Access.VARARGS);
            return this;
        }

        public Builder _native() {
            modifier.set(Access.NATIVE);
            return this;
        }

        public Builder _abstract() {
            modifier.set(Access.ABSTRACT);
            return this;
        }

        public Builder strict() {
            modifier.set(Access.STRICT);
            return this;
        }

        public Builder synthetic() {
            modifier.set(Access.SYNTHETIC);
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

        public MethodModifier build() {
            return modifier;
        }
    }
}
