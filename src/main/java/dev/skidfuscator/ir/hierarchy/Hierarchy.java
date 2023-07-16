package dev.skidfuscator.ir.hierarchy;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.MethodDescriptor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

public interface Hierarchy {
    KlassNode findClass(final String name);

    default KlassNode findClass(final ClassNode node) {
        return findClass(node.name);
    }

    void resolveKlassEdges(final KlassNode klassNode);

    FunctionNode findMethod(final MethodDescriptor methodDescriptor);

    default FunctionNode findMethod(final String owner, final String name, final String desc) {
        return findMethod(new MethodDescriptor(owner, name, desc));
    }

    default FunctionNode findMethod(final MethodInsnNode node) {
        return findMethod(node.owner, node.name, node.desc);
    }
}
