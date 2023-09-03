package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.DirectModifier;
import dev.skidfuscator.ir.access.impl.MethodModifier;

public sealed interface DirectMethodModifier extends DirectModifier<MethodModifier> permits Method {

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

    default boolean isSynchronized() {
        return getModifier().isSynchronized();
    }

    default boolean isBridge() {
        return getModifier().isBridge();
    }

    default boolean isVarArgs() {
        return getModifier().isVarArgs();
    }

    default boolean isNative() {
        return getModifier().isNative();
    }

    default boolean isAbstract() {
        return getModifier().isAbstract();
    }

    default boolean isStrict() {
        return getModifier().isStrict();
    }

    default boolean isSynthetic() {
        return getModifier().isSynthetic();
    }

    default boolean isMandated() {
        return getModifier().isMandated();
    }

    default boolean isDeprecated() {
        return getModifier().isDeprecated();
    }
}
