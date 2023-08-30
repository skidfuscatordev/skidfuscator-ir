package dev.skidfuscator.ir;

import dev.skidfuscator.ir.klass.KlassTags;
import dev.skidfuscator.ir.klass.KlassVisitor;
import dev.skidfuscator.ir.method.MethodVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Klass extends KlassVisitor {
    private String name;
    private Set<KlassTags> tags;
    private Klass parent;
    private List<Klass> interfaces;
    private String signature;
    private List<Method> methods;
    private List<Field> fields;

    public Klass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return "L" + name + ";";
    }

    public boolean isStatic() {
        return tags.contains(KlassTags.STATIC);
    }

    public void setStatic(boolean value) {
        this._tag(KlassTags.STATIC, value);
    }

    public boolean isPublic() {
        return tags.contains(KlassTags.PUBLIC);
    }

    public void setPublic(boolean value) {
        if (value) {
            for (KlassTags tag : new KlassTags[]{KlassTags.PRIVATE, KlassTags.PROTECTED}) {
                _tag(tag, false);
            }
        }

        _tag(KlassTags.PUBLIC, value);
    }

    public boolean isProtected() {
        return tags.contains(KlassTags.PROTECTED);
    }

    public void setProtected(boolean value) {
        if (value) {
            for (KlassTags tag : new KlassTags[]{KlassTags.PRIVATE, KlassTags.PUBLIC}) {
                _tag(tag, false);
            }
        }

        _tag(KlassTags.PROTECTED, value);
    }

    public boolean isPrivate() {
        return tags.contains(KlassTags.PRIVATE);
    }

    public void setPrivate(boolean value) {
        if (value) {
            for (KlassTags tag : new KlassTags[]{KlassTags.PUBLIC, KlassTags.PROTECTED}) {
                _tag(tag, false);
            }
        }

        _tag(KlassTags.PRIVATE, value);
    }

    private void _tag(final KlassTags tag, boolean value) {
        if (value) {
            tags.remove(tag);
        } else {
            tags.add(tag);
        }
    }

    public boolean isInterface() {
        return tags.contains(KlassTags.INTERFACE);
    }

    public void setInterface(boolean value) {
        this._tag(KlassTags.INTERFACE, value);
    }

    public boolean isAbstract() {
        return tags.contains(KlassTags.ABSTRACT);
    }

    public void setAbstract(boolean value) {
        this._tag(KlassTags.ABSTRACT, value);
    }

    public List<Method> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public boolean isPrimitive() {
        return false;
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

    public Method resolveMethod(final String name, final List<Klass> args, final Klass returnType) {
        final Set<Method> methods = this.methods.stream()
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
    public void visit(String name, Klass parent, List<Klass> interfaces, Set<KlassTags> tags, String signature) {
        this.name = name;
        this.parent = parent;
        this.interfaces = interfaces;
        this.tags = tags;
        this.signature = signature;

        super.visit(name, parent, interfaces, tags, signature);
    }

    public void visit(final KlassVisitor visitor) {
        visitor.visit(name, parent, interfaces, tags, signature);
    }

    @Override
    public MethodVisitor visitMethod() {
        final Method method = new Method();
        this.methods.add(method);

        return method;
    }
}
