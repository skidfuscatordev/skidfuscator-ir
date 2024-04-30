package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.JavaMethod;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MethodGroup {
    private String name;
    private final List<JavaMethod> methods;

    private List<Klass> args;
    private Klass returnType;

    public MethodGroup(String name, List<JavaMethod> methods, List<Klass> args, Klass returnType) {
        this.name = name;
        this.methods = methods;
        this.args = args;
        this.returnType = returnType;
    }

    public static Builder of() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull final String name) {
        Assert.nonNull(name, "Name cannot be null");

        this.name = name;
    }

    public List<Klass> getArgs() {
        return args;
    }

    public void setArgs(@NotNull List<Klass> args) {
        Assert.nonNull(args, "Args cannot be null");

        this.args = args;
    }

    public @NotNull Klass getReturnType() {
        return returnType;
    }

    public void setReturnType(@NotNull Klass returnType) {
        Assert.nonNull(returnType, "Return type cannot be null");

        this.returnType = returnType;
    }

    public void addMethod(@NotNull final JavaMethod method) {
        Assert.nonNull(method, "Specified method cannot be null");
        Assert.nonNull(method.getGroup(), "Specified method cannot belong to a different group");
        Assert.eq(name, method.getName(), "Method and group have different names!");

        //TODO: Make sure this works
        Assert.eq(args, method.getArgs(), "Specified method does not have the same descriptor!");

//        final List<Klass> otherArgs = method.getArgs();
//        for (int i = 0; i < args.size(); i++) {
//            final Klass parent = this.args.get(i);
//            final Klass other = otherArgs.get(i);
//
//            Assert.eq(parent, other, "Specified method does not have the same descriptor!");
//        }

        Assert.eq(this.getReturnType(), method.getReturnType(), "Specified method does not have the same return type!");

        this.methods.add(method);
        method.setGroup(this);
    }

    public void removeMethod(@NotNull final JavaMethod method) {
        Assert.nonNull(method, "Specified method cannot be null");
        Assert.eq(this, method.getGroup(), "Method does not belong to this group!");

        this.methods.remove(method);
        method.setGroup(null);
    }

    public boolean hasMethod(@NotNull final JavaMethod method) {
        Assert.nonNull(method, "Specified method cannot be null");

        return this.methods.contains(method);
    }

    public static final class Builder {
        private String name;
        private List<JavaMethod> methods;
        private List<Klass> args;
        private Klass returnType;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder methods(List<JavaMethod> methods) {
            this.methods = methods;
            return this;
        }

        public Builder args(List<Klass> args) {
            this.args = args;
            return this;
        }

        public Builder returnType(Klass returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder but() {
            return of().name(name).methods(methods).args(args).returnType(returnType);
        }

        public MethodGroup build() {
            return new MethodGroup(name, methods, args, returnType);
        }
    }
}
