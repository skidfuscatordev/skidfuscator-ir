package dev.skidfuscator.ir.util;

import java.util.Objects;

public class ClassDescriptor {
    private String owner;
    private String name;
    private String desc;

    public ClassDescriptor(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassDescriptor that)) return false;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(desc, that.desc)) return false;
        return Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return owner + "#" + name + desc;
    }
}
