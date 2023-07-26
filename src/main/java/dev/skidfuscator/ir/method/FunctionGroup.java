package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.type.TypeWrapper;

import java.util.Objects;
import java.util.UUID;

public class FunctionGroup {
    private final UUID id;
    private String name;
    private TypeWrapper desc;

    public FunctionGroup(String name, TypeWrapper desc) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeWrapper getDesc() {
        return desc;
    }

    public void setDesc(TypeWrapper desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionGroup that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
