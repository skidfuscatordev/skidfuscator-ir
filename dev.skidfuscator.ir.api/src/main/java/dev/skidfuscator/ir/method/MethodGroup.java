package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MethodGroup {
    private String name;
    private List<Method> methods;

    private List<Klass> args;
    private Klass returnType;

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

    public void addMethod(@NotNull final Method method) {
        Assert.nonNull(method, "Specified method cannot be null");
        Assert.nonNull(method.getGroup(),"Specified method cannot belong to a different group");
        Assert.eq(name, method.getName(), "Method and group have different names!");

        final List<Klass> otherArgs = method.getArgs();
        for (int i = 0; i < this.args.size(); i++) {
            final Klass _this = this.args.get(i);
            final Klass _other = otherArgs.get(i);

            Assert.eq(_this, _other, "Specified method does not have the same descriptor!");
        }

        Assert.eq(this.getReturnType(), method.getReturnType(), "Specified method does not have the same return type!");

        this.methods.add(method);
        method.setGroup(this);
    }

    public void removeMethod(@NotNull final Method method) {
        Assert.nonNull(method, "Specified method cannot be null");
        Assert.eq(this, method.getGroup(), "Method does not belong to this group!");

        this.methods.remove(method);
        method.setGroup(null);
    }

    public boolean hasMethod(@NotNull final Method method) {
        Assert.nonNull(method, "Specified method cannot be null");

        return this.methods.contains(method);
    }

    public static MethodGroupBuilder of() {
        return new MethodGroupBuilder();
    }


    public static final class MethodGroupBuilder {
        private String name;
        private List<Method> methods;
        private List<Klass> args;
        private Klass returnType;

        private MethodGroupBuilder() {
        }

        public MethodGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MethodGroupBuilder methods(List<Method> methods) {
            this.methods = methods;
            return this;
        }

        public MethodGroupBuilder args(List<Klass> args) {
            this.args = args;
            return this;
        }

        public MethodGroupBuilder returnType(Klass returnType) {
            this.returnType = returnType;
            return this;
        }

        public MethodGroupBuilder but() {
            return of().name(name).methods(methods).args(args).returnType(returnType);
        }

        public MethodGroup build() {
            MethodGroup methodGroup = new MethodGroup();
            methodGroup.setName(name);
            methodGroup.setArgs(args);
            methodGroup.setReturnType(returnType);
            methodGroup.methods = this.methods;
            return methodGroup;
        }
    }
}
