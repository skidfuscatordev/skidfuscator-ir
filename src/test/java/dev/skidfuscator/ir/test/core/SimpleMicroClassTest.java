package dev.skidfuscator.ir.test.core;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.impl.SkidHierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.test.testclasses.SimpleMicroClass;
import dev.skidfuscator.ir.test.util.ClassHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;

import java.util.Arrays;

public class SimpleMicroClassTest {

    private Hierarchy hierarchy;
    private KlassNode microKlassNode;

    @BeforeEach
    public void setup() {
        this.hierarchy = new SkidHierarchy();

        final ClassNode microClassNode = ClassHelper.create(
                SimpleMicroClass.class
        );
        hierarchy.resolveClasses(Arrays.asList(
                ClassHelper.create(Object.class),
                microClassNode
        ));

        microKlassNode = hierarchy.findClass(
                SimpleMicroClass.class.getName().replace('.', '/')
        );
    }

    @Test
    public void testInit() {
        // cannot be null
        assert microKlassNode != null : "Micro class node is null";

        // self <init>()
        // void test1()
        // void test2()
        assert microKlassNode.getMethods().size() == 4 : "Micro class node has invalid method count";
    }

    @Test
    public void testHierarchy() {
        assert microKlassNode.getParent().equals(
                hierarchy.findClass(Object.class.getName().replace('.', '/'))
        ) : "Micro class node has invalid parent";

        assert microKlassNode.getInterfaces().isEmpty()
                : "Micro class node has invalid implementation count";

        final FunctionNode initNode = microKlassNode.getMethods().get(0);

        assert initNode != null : "Micro class node has invalid <init> node";
        assert initNode.getName().equals("<init>")
                : "Micro class node has invalid <init> node name";
        assert initNode.getDesc().equals("()V");
        assert initNode.getParent().equals(microKlassNode);
        assert initNode.getGroup().getDesc().equals("()V");
        assert initNode.getGroup().getName().equals("<init>");
    }

    @Test
    public void testInitHierarchy() {
        final KlassNode rootNode = hierarchy.findClass("java/lang/Object");
        /*
         * small sanity checks
         */
        assert rootNode != null : "Root node is null";

        final FunctionNode initNode = rootNode.getMethods().get(0);
        /*
         * small sanity checks
         */
        assert initNode != null : "Root node has invalid <init> node";
        assert initNode.getName().equals("<init>")
                : "Root node has invalid <init> node name";
        assert initNode.getDesc().equals("()V");
        assert initNode.getParent().equals(rootNode);

        final FunctionNode microInitNode = microKlassNode.getMethods().get(0);
        /*
         * small sanity checks
         */
        assert microInitNode != null : "Micro class node has invalid <init> node";
        assert microInitNode.getName().equals("<init>")
                : "Micro class node has invalid <init> node name";
        assert microInitNode.getDesc().equals("()V");
        assert microInitNode.getParent().equals(microKlassNode);

        /*
         * Root node <init> group is equal to micro class node <init> group
         * is a bad scenario as constructors are class unique and are recursively
         * called until parent is reached.
         *
         * This means that function groups do NOT apply to this method.
         */
        assert !initNode.getGroup().equals(microInitNode.getGroup())
                : "Root node <init> group is equal to micro class node <init> group";
    }
}
