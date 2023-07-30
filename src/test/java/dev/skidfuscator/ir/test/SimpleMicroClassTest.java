package dev.skidfuscator.ir.test;

import dev.skidfuscator.ir.insn.impl.FieldInsn;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.field.FieldInvoker;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.test.core.RuntimeClassPathHierarchy;
import dev.skidfuscator.ir.util.ClassHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
public class SimpleMicroClassTest {

    private Hierarchy hierarchy;
    private KlassNode microKlassNode;

    @BeforeEach
    public void setup() {
        this.hierarchy = new RuntimeClassPathHierarchy();

        final ClassNode microClassNode = ClassHelper.create(
                SimpleMicroClass.class
        );
        hierarchy.resolveClasses(Arrays.asList(
                microClassNode
        ));

        microKlassNode = hierarchy.findClass(
                SimpleMicroClass.class.getName().replace('.', '/')
        );
    }

    @Test
    public void testInitMethods() {
        // cannot be null
        assertNotNull(microKlassNode);

        // self <init>()
        // void test1()
        // void test2()
        /*microKlassNode.getMethods()
                .stream()
                .filter(e -> !e.isResolved())
                .forEach(e -> {
                    throw new IllegalStateException(String.format(
                            "Method %s#%s is not resolved!",
                            e.getOwner().getName(),
                            e.getName() + e.getDesc()
                    ));
                });*/
        final Collection<FunctionNode> methods = microKlassNode.getMethods()
                .stream()
                .filter(e -> !e.isSynthetic())
                .collect(Collectors.toList());
        assertEquals(4, methods.size(), () -> "Invalid method count (expected 4 but was "
                + methods.size() + "): \n --> "
                + methods.stream().map(Objects::toString).collect(Collectors.joining("\n --> ")));
    }

    @Test
    public void testInitFields() {
        // cannot be null
        assertNotNull(microKlassNode);

        // assert there is only one field
        assertEquals(1, microKlassNode.getFields().size());
    }

    @Test
    public void testHierarchy() {
        assert microKlassNode.getParent().equals(
                hierarchy.findClass(Object.class.getName().replace('.', '/'))
        ) : "Micro class node has invalid parent";

        assert microKlassNode.getInterfaces().isEmpty()
                : "Micro class node has invalid implementation count";

        final FunctionNode initNode = microKlassNode.getMethod("<init>()V");

        assert initNode != null :
                "Micro class node has invalid <init> node! Available: \n->"
                        + microKlassNode.getMethods().stream().map(Object::toString).collect(Collectors.joining("\n->"));
        assert initNode.getName().equals("<init>")
                : "Micro class node has invalid <init> node name";
        assert initNode.getDesc().equals("()V");
        assert initNode.getOwner().equals(microKlassNode);
    }

    @Test
    public void testInitHierarchy() {
        final KlassNode rootNode = hierarchy.findClass("java/lang/Object");
        /*
         * small sanity checks
         */
        assert rootNode != null : "Root node is null";

        final FunctionNode initNode = rootNode.getMethod("<init>()V");
        /*
         * small sanity checks
         */
        assert initNode != null : "Root node has invalid <init> node";
        assert initNode.getName().equals("<init>")
                : "Root node has invalid <init> node name";
        assert initNode.getDesc().equals("()V");
        assert initNode.getOwner().equals(rootNode);

        final FunctionNode microInitNode = microKlassNode.getMethod("<init>()V");
        /*
         * small sanity checks
         */
        assert microInitNode != null : "Micro class node has invalid <init> node";
        assert microInitNode.getName().equals("<init>")
                : "Micro class node has invalid <init> node name";
        assert microInitNode.getDesc().equals("()V");
        assert microInitNode.getOwner().equals(microKlassNode);
    }

    @Test
    public void testFieldResolve() {
        final FieldNode fieldNode = microKlassNode.getFields().iterator().next();

        final FunctionNode initNode = microKlassNode.getMethod("<init>()V");

        System.out.println("\n\n---- Init: ");
        for (Insn instruction : initNode.getInstructions()) {
            System.out.println(instruction);
        }
        System.out.println("------------------\n");

        final FunctionNode invokeNode = microKlassNode.getMethod("test2()V");
        System.out.printf("\n\n---- %s: %n", invokeNode);
        for (Insn instruction : invokeNode.getInstructions()) {
            System.out.println(instruction);
        }
        System.out.println("------------------\n");

        // Has one invoker in class
        for (FieldInvoker<?, ?> invoker : fieldNode.getInvokers()) {
            System.out.println(((FieldInsn) invoker.get()).getParent().getNode() + "\n|\n\\->" + invoker + "\n");
        }

        // one invoker in <init>
        // one invoker in test()
        assertEquals(2, fieldNode.getInvokers().size());
    }

    public static class SimpleMicroClass {
        private int field = 69;

        public static void main(String[] args) {
            new SimpleMicroClass().test();
        }

        public void test() {
            System.out.println("Hello World!");
            this.field = 90;
        }

        public void test2() {
            System.out.println("Hello World!");
        }
    }
}
