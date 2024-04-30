package dev.skidfuscator.ir;

import dev.skidfuscator.ir.access.impl.ClassModifier;
import dev.skidfuscator.ir.klass.KlassVisitor;
import dev.skidfuscator.ir.method.MethodVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public non-sealed class Klass extends KlassVisitor implements DirectClassModifier {
    private String name;
    private Klass parent;

    private ClassModifier modifier;

    private List<Klass> interfaces;
    private String signature;
    private List<JavaMethod> methods;
    private List<Field> fields;

    public Klass(String name) {
        this.name = name;
    }

    public Field resolveField(final String name, final Klass type) {
        final Set<Field> fields = this.fields.stream()
                .filter(e -> e.getName().equals(name) && e.getType().equals(type))
                .collect(Collectors.toSet());

        if (fields.isEmpty()) {
            throw new IllegalStateException(String.format(
                    "Field of name %s of type %s was not found in %s",
                    name,
                    type,
                    this
            ));
        }

        if (fields.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Field of name %s of type %s has duplicate definitions in %s: %s",
                    name,
                    type,
                    this,
                    Arrays.toString(fields.toArray())
            ));
        }

        return fields.iterator().next();
    }

    public JavaMethod resolveMethod(final String name, final List<Klass> args, final Klass returnType) {
        final Set<JavaMethod> methods = this.methods.stream()
                .filter(e -> e.getName().equals(name) && e.getArgs().equals(args) && e.getReturnType().equals(returnType))
                .collect(Collectors.toSet());

        if (methods.isEmpty()) {
            throw new IllegalStateException(String.format(
                    "Method of name %s of args %s and return type %s was not found in %s",
                    name,
                    Arrays.toString(args.toArray()),
                    returnType,
                    this
            ));
        }

        if (methods.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Method of name %s of args %s and return type %s has duplicate definitions in %s: %s",
                    name,
                    Arrays.toString(args.toArray()),
                    returnType,
                    this,
                    Arrays.toString(methods.toArray())
            ));
        }

        return methods.iterator().next();
    }

    @Override
    public void visit(String name, Klass parent, List<Klass> interfaces, ClassModifier modifier, String signature) {
        this.name = name;
        this.parent = parent;
        this.interfaces = interfaces;
        this.modifier = modifier;
        this.signature = signature;

        super.visit(name, parent, interfaces, modifier, signature);
    }

    public void visit(final KlassVisitor visitor) {
        visitor.visit(name, parent, interfaces, modifier, signature);
    }

    @Override
    public MethodVisitor visitMethod() {
        final JavaMethod method = new JavaMethod();
        this.methods.add(method);

        return method;
    }

    @Override
    public ClassModifier getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(ClassModifier modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return "L" + name + ";";
    }

    public List<JavaMethod> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public boolean isPrimitive() {
        return false;
    }
}
