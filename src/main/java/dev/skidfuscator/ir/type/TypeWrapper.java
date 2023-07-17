package dev.skidfuscator.ir.type;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;

import java.util.LinkedList;
import java.util.List;

/*
I still don't have idea how i want it to be
This is stupid help me xd

TODO: Add support for "java/lang/String"
TODO: Unit tests
TODO: Basic fallback if hierarchy#findClass fails
 */
public class TypeWrapper implements HierarchyResolvable {

    private final Type type;
    private final Hierarchy hierarchy;

    private String desc = "";
    private final List<KlassNode> classes = new LinkedList<>();

    public TypeWrapper(Type type, Hierarchy hierarchy) {
        this.type = type;
        this.hierarchy = hierarchy;
    }

    @Override
    public void resolve() {
        if (type.getSort() == Type.METHOD) {
            desc += "(";
            for (Type argumentType : type.getArgumentTypes()) {
                resolve0(argumentType);
            }
            desc += ")";
            resolve0(type.getReturnType());
        } else {
            resolve0(type);
        }
    }

    private void resolve0(Type type) {
        switch (type.getSort()) {
            case Type.ARRAY -> {
                Type element = type.getElementType();
                desc += "[".repeat(type.getDimensions());
                if (element.getSort() == Type.OBJECT) {
                    desc += "%s"; //className?
                    classes.add(hierarchy.findClass(type.getElementType().getInternalName()));
                } else {
                    desc += element.getInternalName();
                }
            }
            case Type.OBJECT -> {
                desc += "%s"; //className?
                classes.add(hierarchy.findClass(type.getInternalName()));
            }
            default -> desc += type.getInternalName();
        }
    }

    public String getDesc() {
        return desc;
    }

    public Type dump() {
        return !classes.isEmpty() ? Type.getType(desc.formatted(
                (Object[]) classes.stream().map(klassNode -> "L" + klassNode.getName() + ";").toArray(String[]::new)
        )) : Type.getType(desc);
    }
}
