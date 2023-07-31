package dev.skidfuscator.ir.type;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Type;

import java.util.Arrays;
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
    private boolean resolved = false;
    private final List<KlassNode> classes = new LinkedList<>();

    public TypeWrapper(Type type, Hierarchy hierarchy) {
        this.type = type;
        this.hierarchy = hierarchy;
    }

    @Override
    public void resolveHierarchy() {
        if (resolved) {
            throw new IllegalStateException(String.format(
                    "Type %s is already resolved",
                    type.getDescriptor()
            ));
        }
        resolved = true;

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

        //System.out.printf("Resolved type %s --> %s\n", type.getDescriptor(), desc);
    }

    public boolean isResolved() {
        return resolved;
    }

    private void resolve0(Type type) {
        switch (type.getSort()) {
            case Type.ARRAY -> {
                Type element = type.getElementType();
                desc += "[".repeat(type.getDimensions());
                if (element.getSort() == Type.OBJECT) {
                    desc += "%s"; //className?

                    final KlassNode klassNode = hierarchy.resolveClass(element.getInternalName());
                    if (klassNode == null) {
                        throw new IllegalStateException(String.format(
                                "Could not find class for type %s",
                                element.getInternalName()
                        ));
                    }

                    classes.add(klassNode);
                } else {
                    desc += element.getInternalName();
                }
                break;
            }
            case Type.OBJECT -> {
                desc += "%s"; //className?

                final KlassNode klassNode = hierarchy.resolveClass(type.getInternalName());
                if (klassNode == null) {
                    throw new IllegalStateException(String.format(
                            "Could not find class for type %s",
                            type.getInternalName()
                    ));
                }
                classes.add(klassNode);
                break;
            }
            default -> desc += type.getInternalName();
        }
    }

    public Type getOriginalType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public Type dump() {
        if (!resolved) {
            throw new IllegalStateException(String.format(
                    "Unresolved type %s",
                    type.getDescriptor()
            ));
        }

        if (classes.isEmpty()) {
            return Type.getType(desc);
        }

        final String[] types = classes.stream().map(klassNode -> "L" + klassNode.getName() + ";").toArray(String[]::new);
        //System.out.println(String.format("OG: %s DESC: %s TYPES: %s", type.getDescriptor(), desc, Arrays.toString(types)));
        return Type.getType(desc.formatted((Object[]) types));
    }
}
