package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.method.impl.ResolvedImmutableFunctionNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class ArraySpecialKlassNode implements KlassNode {
    private final Hierarchy hierarchy;
    private final KlassNode parent;
    private final int dimensions;
    private final Map<Descriptor, FunctionNode> methods;

    public ArraySpecialKlassNode(Hierarchy hierarchy, int dimensions, KlassNode parent) {
        this.hierarchy = hierarchy;
        this.parent = parent;
        this.dimensions = dimensions;
        this.methods = new HashMap<>();

    }

    @Override
    public boolean isResolvedHierarchy() {
        return true;
    }

    @Override
    public boolean isResolvedInternal() {
        return true;
    }

    @Override
    public void resolveHierarchy() {
        final Descriptor descriptor = new Descriptor("clone", "()Ljava/lang/Object;");
        final FunctionNode cloneFunction = new ResolvedImmutableFunctionNode(
                hierarchy,
                descriptor,
                new MethodNode(
                        Opcodes.ACC_PUBLIC,
                        descriptor.getName(),
                        descriptor.getDesc(),
                        null,
                        null
                )
        );
        cloneFunction.setOwner(this);
        this.hierarchy.addMethod(cloneFunction);
        methods.put(
                descriptor,
                cloneFunction
        );
    }

    @Override
    public void lock() {
        throw new IllegalStateException("Cannot lock an array class");
    }

    @Override
    public void resolveInternal() {
        throw new IllegalStateException("Cannot set internal of an array class");
    }

    @Override
    public void resolveInstructions() {
        throw new IllegalStateException("Cannot resolve instructions of an array class");
    }

    @Override
    public @NotNull Type asType() {
        return Type.getType("[".repeat(this.dimensions) + parent.asType().getInternalName());
    }

    @Override
    public @NotNull Collection<FunctionNode> getMethods() {
        return this.methods == null
                ? Collections.emptySet()
                : Collections.unmodifiableCollection(methods.values());
    }

    @Override
    public @NotNull FunctionNode getMethod(String name, String desc) {
        return this.methods.get(new Descriptor(name, desc));
    }

    @Override
    public @NotNull List<FieldNode> getFields() {
        throw new IllegalStateException("Cannot modify fields of an array class");
    }

    @Override
    public @NotNull FieldNode getField(String name, String desc) {
        throw new IllegalStateException("Cannot get field of an array class");
    }


    @Override
    public void addMethod(FunctionNode node) {
        throw new IllegalStateException("Cannot adds methods to an array class");
    }

    @Override
    public void removeMethod(FunctionNode node) {
        throw new IllegalStateException("Cannot remove methods to an array class");
    }

    @Override
    public void addField(FieldNode node) {
        throw new IllegalStateException("Cannot add field to an array class");
    }

    @Override
    public void removeField(FieldNode node) {
        throw new IllegalStateException("Cannot remove field to an array class");
    }

    @Override
    public @NotNull String getName() {
        return "[".repeat(this.dimensions) + this.parent.asType().getDescriptor();
    }

    @Override
    public void setName(final @NotNull String name) {
        throw new IllegalStateException("Cannot set name of an array class");
    }

    @Override
    public String getSignature() {
        return parent.getSignature();
    }

    @Override
    public KlassNode getParent() {
        return null;
    }

    @Override
    public void setParent(KlassNode klass) {
        throw new IllegalStateException("Cannot set parent of an array class");
    }

    @Override
    public @NotNull List<KlassNode> getInterfaces() {
        return Collections.emptyList();
    }

    @Override
    public void setInterfaces(List<KlassNode> klasses) {
        throw new IllegalStateException("Cannot set interfaces of an array class");
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public ClassNode dump() {
        throw new IllegalStateException("Cannot dump implicit array class");
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
