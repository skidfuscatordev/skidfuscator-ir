package dev.skidfuscator.ir.hierarchy.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.method.MethodGraph;
import dev.skidfuscator.ir.method.impl.ResolvedFunctionNode;
import dev.skidfuscator.ir.hierarchy.klass.KlassExtendsEdge;
import dev.skidfuscator.ir.hierarchy.klass.KlassImplementsEdge;
import dev.skidfuscator.ir.klass.KlassGraph;
import dev.skidfuscator.ir.hierarchy.klass.KlassInheritanceEdge;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.impl.ResolvedKlassNode;
import dev.skidfuscator.ir.method.MethodDescriptor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class SkidHierarchy implements Hierarchy {
    /**
     * This will, has and will ALWAYS base itself on
     * LEGACY names. This means any name modification
     * WILL NOT BE RESOLVED IN HERE. This is BY DESIGN.
     * <p>
     * This allows us to do:
     * - Safe renaming
     * - Safe re-parenting
     */
    private final Map<String, KlassNode> classEquivalence;
    private final KlassGraph klassGraph;


    /**
     * This will, has and will ALWAYS base itself on
     * LEGACY names. This means any name modification
     * WILL NOT BE RESOLVED IN HERE. This is BY DESIGN.
     * <p>
     * This allows us to do:
     * - Safe renaming
     * - Safe re-parenting
     */
    private final Map<MethodDescriptor, FunctionNode> functionEquivalence;
    private final MethodGraph functionGraph;


    public SkidHierarchy() {
        this.classEquivalence = new HashMap<>();
        this.functionEquivalence = new HashMap<>();
        this.klassGraph = new KlassGraph();
        this.functionGraph = new MethodGraph();
    }

    /**
     * Resolve classes in a typical BFS top down strategy.
     * This allows for the construction of method groups
     * based on their parents, making it easier to have
     * less conflicts for branches for methods.
     *
     * @param classes Collection of classes to resolve
     */
    private void resolveClasses(final Collection<ClassNode> classes) {
        classes.forEach(this::create);

        final Set<KlassNode> resolved = new HashSet<>();

        final KlassNode root = findClass("java/lang/Object");
        final Queue<KlassNode> queue = new LinkedBlockingQueue<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            final KlassNode node = queue.poll();

            // Prevent double resolution or cyclic dependency
            if (resolved.contains(node))
                continue;

            final List<KlassNode> children = klassGraph
                    .incomingEdgesOf(node)
                    .stream()
                    .map(KlassInheritanceEdge::getNode)
                    .collect(Collectors.toList());

            node.resolve();
            resolved.add(node);

            queue.addAll(children);
        }
    }

    @Override
    public KlassNode findClass(String name) {
        return classEquivalence.get(name);
    }

    @Override
    public FunctionNode findMethod(MethodDescriptor methodDescriptor) {
        return functionEquivalence.get(methodDescriptor);
    }

    public KlassNode create(final ClassNode classNode) {
        KlassNode klassNode = findClass(classNode.name);

        if (klassNode == null) {
            klassNode = new ResolvedKlassNode(this, classNode);
            classEquivalence.put(classNode.name, klassNode);
            klassGraph.addVertex(klassNode);

            for (MethodNode method : classNode.methods) {
                final FunctionNode function = create(klassNode, method);
                klassNode.addMethod(function);
            }
        }

        return klassNode;
    }

    public FunctionNode create(final KlassNode parent, final MethodNode node) {
        final MethodDescriptor descriptor = new MethodDescriptor(
                parent.getName(),
                node.name,
                node.desc
        );

        FunctionNode functionNode = findMethod(descriptor);

        if (functionNode == null) {
            functionNode = new ResolvedFunctionNode(
                    node,
                    this
            );
            functionNode.setParent(parent);

            functionEquivalence.put(descriptor, functionNode);
            functionGraph.addVertex(functionNode);
        }

        return functionNode;
    }

    public void resolveKlassEdges(final KlassNode klassNode) {
        for (KlassInheritanceEdge edge : klassGraph.edgesOf(klassNode)) {
            klassGraph.removeEdge(edge);
        }

        if (klassNode.getParent() != null) {
            final boolean success = klassGraph.addEdge(
                    klassNode,
                    klassNode.getParent(),
                    new KlassExtendsEdge(klassNode, klassNode.getParent())
            );
        }

        for (KlassNode interfaze : klassNode.getInterfaces()) {
            final boolean success = klassGraph.addEdge(
                    klassNode,
                    interfaze,
                    new KlassImplementsEdge(klassNode, klassNode.getParent())
            );
        }
    }

}
