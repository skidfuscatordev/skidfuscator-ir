package dev.skidfuscator.ir.test.core;

import dev.skidfuscator.ir.hierarchy.impl.SkidHierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.util.ClassDescriptor;
import dev.skidfuscator.ir.util.ClassHelper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class RuntimeClassPathHierarchy extends SkidHierarchy {

    @Override
    public FunctionNode findMethod(ClassDescriptor descriptor) {
        return super.findMethod(descriptor);
    }

    @Override
    public KlassNode findClass(String name) {
        KlassNode node = super.findClass(name);

        if (node == null) {
            try {
                final String path = name.replace(".", "/");
                //System.out.println("Loading " + path);
                final ClassNode attempt = ClassHelper.create(path);
                node = create(attempt);
                //System.out.println("Created " + path);
                if (!node.isResolvedHierarchy())
                    node.resolveHierarchy();
                //System.out.println("Resolved " + path);
                if (!node.isResolvedInternal())
                    node.resolveInternal();
                //System.out.println("Loaded " + path);
                return node;
            } catch (IOException e) {
                e.printStackTrace();
                return super.findClass(name);
            } catch (IllegalStateException e) {
                throw e;
            }
        }

        return node;
    }
}
