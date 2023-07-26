package dev.skidfuscator.ir.hierarchy;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Collection;

public interface Hierarchy {
    /**
     * Resolve classes in a typical BFS top down strategy.
     * This allows for the construction of method groups
     * based on their parents, making it easier to have
     * less conflicts for branches for methods.
     *
     * @param classes Collection of classes to resolve
     */
    void resolveClasses(final Collection<ClassNode> classes);

    /**
     * Finds a class with the given name
     *
     * @param name the name of the class to find
     * @return the KlassNode object representing the found class
     */
    KlassNode findClass(final String name);

    /**
     * Finds the class with the specified name.
     *
     * @param node The ClassNode to search for.
     * @return The found ClassNode.
     */
    default KlassNode findClass(final ClassNode node) {
        return findClass(node.name);
    }

    /**
     * Resolves the edges of the specified KlassNode
     *
     * @param klassNode the KlassNode to resolve the edges for
     */
    void resolveKlassEdges(final KlassNode klassNode);

    /**
     * Finds the method with the specified name.
     *
     * @param methodDescriptor The original descriptor of the method being saught
     * @return The found (or not) function node
     */
    FunctionNode findMethod(final Descriptor methodDescriptor);

    default FunctionNode findMethod(final String owner, final String name, final String desc) {
        return findMethod(new Descriptor(owner, name, desc));
    }

    default FunctionNode findMethod(final MethodInsnNode node) {
        return findMethod(node.owner, node.name, node.desc);
    }

    /**
     * Finds the field with the specified name.
     *
     * @param descriptor The descriptor of the field to search for.
     * @return The found FieldNode.
     */
    FieldNode findField(final Descriptor descriptor);

    default FieldNode findField(final String owner, final String name, final String desc) {
        return findField(new Descriptor(owner, name, desc));
    }

    default FieldNode findField(final FieldInsnNode node) {
        return findField(node.owner, node.name, node.desc);
    }
}
