package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.DirectModifier;
import dev.skidfuscator.ir.access.impl.FieldModifier;

public sealed interface DirectFieldModifier extends DirectModifier<FieldModifier> permits Field {

    default void setPublic() {
        getModifier().setPublic();
    }

    default void setPrivate() {
        getModifier().setPrivate();
    }

    default void setProtected() {
        getModifier().setProtected();
    }

    default void setStatic() {
        getModifier().setStatic();
    }

    default void setNotStatic() {
        getModifier().setNotStatic();
    }

    default void clearAccess() {
        getModifier().clearAccess();
    }

    default void resetAccess() {
        getModifier().resetAccess();
    }

    default boolean isPublic() {
        return getModifier().isPublic();
    }

    default boolean isPrivate() {
        return getModifier().isPrivate();
    }

    default boolean isProtected() {
        return getModifier().isProtected();
    }

    default boolean isStatic() {
        return getModifier().isStatic();
    }

    default boolean isFinal() {
        return getModifier().isFinal();
    }

    default boolean isVolatile() {
        return getModifier().isVolatile();
    }

    default boolean isTransient() {
        return getModifier().isTransient();
    }

    default boolean isSynthetic() {
        return getModifier().isSynthetic();
    }

    //TODO: Im not sure what FIELD_INNER means in asm xd
    default boolean isEnum() {
        return getModifier().isEnum();
    }

    default boolean isMandated() {
        return getModifier().isMandated();
    }

    default boolean isDeprecated() {
        return getModifier().isDeprecated();
    }
}
