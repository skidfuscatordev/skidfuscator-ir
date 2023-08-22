package dev.skidfuscator.ir.verify;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Assert {
    @Contract("null -> fail")
    public static void nonNull(final Object var) {
        nonNull(var, "Specified object cannot be null");
    }

    @Contract("null, _ -> fail")
    public static void nonNull(final Object var, @NotNull final String error) {
        nonNull(var, new IllegalStateException(error));
    }

    @Contract("null, _ -> fail")
    public static void nonNull(final Object var, @NotNull final RuntimeException error) {
        if (var == null)
            throw error;
    }

    @Contract("false, _ -> fail")
    public static void isTrue(final boolean var, @NotNull final String error) {
        if (!var)
            throw new IllegalStateException(error);
    }

    @Contract("false, _ -> fail")
    public static void isTrue(final boolean var, @NotNull final RuntimeException e) {
        if (!var)
            throw e;
    }

    @Contract("null, !null -> fail; !null, null -> fail")
    public static void eq(final Object a, final Object b) {
        eq(a, b, () -> String.format("Objects are not equal! %s vs %s", format(a), format(b)));
    }

    @Contract("null, !null, _ -> fail; !null, null, _ -> fail")
    public static void eq(final Object a, final Object b, final String error) {
        eq(a, b, () -> String.format("%s\n--> %s vs %s", error, format(a), format(b)));
    }

    @Contract("null, !null, _ -> fail; !null, null, _ -> fail")
    public static void eq(final Object a, final Object b, final Supplier<String> error) {
        if (a.equals(b))
            return;

        throw new IllegalStateException(error.get());
    }

    private static String format(Object o) {
        if (o instanceof List<?>) {
            return Arrays.toString(((List<?>) o).toArray());
        }

        return o.toString();
    }
}
