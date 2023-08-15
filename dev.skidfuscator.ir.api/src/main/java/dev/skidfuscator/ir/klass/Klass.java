package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.Method;

import java.util.List;
import java.util.Set;

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

    public List<Method> getMethods() {
        return methods;
    }

    public List<Field> getFields() {
        return fields;
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
}
