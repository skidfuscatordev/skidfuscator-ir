package dev.skidfuscator.ir.hierarchy;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.field.FieldDescriptor;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.MethodDescriptor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public interface Hierarchy {
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

    FieldNode findField(final FieldDescriptor fieldDescriptor);

    default FieldNode findField(final String owner, final String name, final String desc) {
        return findField(new FieldDescriptor(owner, name, desc));
    }

    default FieldNode findField(final FieldInsnNode node) {
        return findField(node.owner, node.name, node.desc);
    }
}
