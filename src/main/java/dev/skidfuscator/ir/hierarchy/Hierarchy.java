package dev.skidfuscator.ir.hierarchy;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.MethodDescriptor;
import org.objectweb.asm.tree.ClassNode;
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

    FunctionNode findMethod(final MethodDescriptor methodDescriptor);

    default FunctionNode findMethod(final String owner, final String name, final String desc) {
        return findMethod(new MethodDescriptor(owner, name, desc));
    }

    default FunctionNode findMethod(final MethodInsnNode node) {
        return findMethod(node.owner, node.name, node.desc);
    }
}
