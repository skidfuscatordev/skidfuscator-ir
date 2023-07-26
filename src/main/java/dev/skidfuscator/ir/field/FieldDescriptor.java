package dev.skidfuscator.ir.field;

import java.util.Objects;

public class FieldDescriptor {
    private final String owner;
    private final String name;
    private final String desc;

    public FieldDescriptor(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldDescriptor that)) return false;

        if (!Objects.equals(owner, that.owner)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}