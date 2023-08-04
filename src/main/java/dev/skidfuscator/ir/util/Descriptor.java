package dev.skidfuscator.ir.util;

import dev.skidfuscator.ir.field.FieldNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.util.Objects;

public class Descriptor {
    public static Descriptor of(FieldNode fieldNode) {
        return new Descriptor(fieldNode.getName(), fieldNode.getDesc());
    }
    public static Descriptor of(MethodNode methodNode) {
        return new Descriptor(methodNode.name, methodNode.desc);
    }
    public static Descriptor of(String name, String desc) {
        return new Descriptor(name, desc);
    }
    private final String name;
    private final String desc;
    private final String param;

    public Descriptor(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.param = desc.split("\\)")[0].substring(1);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    private String getParam() {
        return param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Descriptor that)) return false;

        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + desc;
    }
}
