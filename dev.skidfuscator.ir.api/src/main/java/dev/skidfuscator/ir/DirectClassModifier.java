package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.DirectModifier;
import dev.skidfuscator.ir.access.impl.ClassModifier;

public sealed interface DirectClassModifier extends DirectModifier<ClassModifier> permits Klass {

    default void setPublic() {
        getModifier().setPublic();
    }

    default void setPrivate() {
        getModifier().setPrivate();
    }

    default void setProtected() {
        getModifier().setProtected();
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

    default boolean isFinal() {
        return getModifier().isFinal();
    }

    default boolean isSuper() {
        return getModifier().isSuper();
    }

    default boolean isInterface() {
        return getModifier().isInterface();
    }

    default boolean isAbstract() {
        return getModifier().isAbstract();
    }

    default boolean isSynthetic() {
        return getModifier().isSynthetic();
    }

    default boolean isAnnotation() {
        return getModifier().isAnnotation();
    }

    default boolean isEnum() {
        return getModifier().isEnum();
    }

    default boolean isMandated() {
        return getModifier().isMandated();
    }

    default boolean isModule() {
        return getModifier().isModule();
    }

    default boolean isRecord() {
        return getModifier().isRecord();
    }

    default boolean isDeprecated() {
        return getModifier().isDeprecated();
    }
}
