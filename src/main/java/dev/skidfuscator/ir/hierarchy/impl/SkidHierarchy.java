package dev.skidfuscator.ir.hierarchy.impl;

import dev.skidfuscator.ir.klass.impl.ArraySpecialKlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.field.impl.ResolvedFieldNode;
import dev.skidfuscator.ir.klass.impl.UnresolvedKlassNode;
import dev.skidfuscator.ir.method.FunctionGraph;
import dev.skidfuscator.ir.hierarchy.klass.KlassExtendsEdge;
import dev.skidfuscator.ir.hierarchy.klass.KlassImplementsEdge;
import dev.skidfuscator.ir.klass.KlassGraph;
import dev.skidfuscator.ir.hierarchy.klass.KlassInheritanceEdge;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.impl.ResolvedKlassNode;
import dev.skidfuscator.ir.util.ClassDescriptor;
import dev.skidfuscator.ir.util.ClassHelper;
import org.objectweb.asm.tree.ClassNode;

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
    protected final Map<String, KlassNode> classEquivalence;
    private final KlassGraph klassGraph;
    private final KlassNode rootClass;
    private final KlassNode arrayClass;


    /**
     * This will, has and will ALWAYS base itself on
     * LEGACY names. This means any name modification
     * WILL NOT BE RESOLVED IN HERE. This is BY DESIGN.
     * <p>
     * This allows us to do:
     * - Safe renaming
     * - Safe re-parenting
     */
    private final Map<ClassDescriptor, FunctionNode> functionEquivalence;
    private final FunctionGraph functionGraph;

    /**
     * This will, has and will ALWAYS base itself on
     * LEGACY names. This means any name modification
     * WILL NOT BE RESOLVED IN HERE. This is BY DESIGN.
     * <p>
     * This allows us to do:
     * - Safe renaming
     * - Safe re-parenting
     */
    private final Map<ClassDescriptor, FieldNode> fieldEquivalence;


    public SkidHierarchy() {
        this.classEquivalence = new HashMap<>();
        this.functionEquivalence = new HashMap<>();
        this.fieldEquivalence = new HashMap<>();
        this.klassGraph = new KlassGraph();
        this.functionGraph = new FunctionGraph();

        this.rootClass = this.create(ClassHelper.create(Object.class));
        this.arrayClass = new ArraySpecialKlassNode(this);

    }

    @Override
    public Iterable<KlassNode> iterateKlasses() {
        return Collections.unmodifiableCollection(classEquivalence.values());
    }

    @Override
    public void resolveClasses(final Collection<ClassNode> classes) {
        classes.forEach(this::create);
        new HashSet<>(classEquivalence.values())
                .stream()
                .filter(e -> !e.isResolvedHierarchy())
                .forEach(KlassNode::resolveHierarchy);
        final Set<String> application = classes.stream().map(e -> e.name).collect(Collectors.toSet());
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

            if (!node.isResolvedInternal())
                node.resolveInternal();

            if (application.contains(node.getName()))
                node.resolveInstructions();
            System.out.println("/!\\ Resolved instructions " + node.getName());
            resolved.add(node);

            queue.addAll(children);
        }
    }

    @Override
    public KlassNode findClass(String name) {
        // Hotload the parent
        if (name.startsWith("[")) {
            return arrayClass;
        }

        final KlassNode node = classEquivalence.get(name);

        return node;
    }

    @Override
    public KlassNode resolveClass(String name) {
        KlassNode node = findClass(name);

        if (node != null) {
            return node;
        }

        node = new UnresolvedKlassNode(rootClass, name);
        classEquivalence.put(name, node);
        klassGraph.addVertex(node);

        return node;
    }

    @Override
    public FunctionNode findMethod(ClassDescriptor descriptor) {
        // Hotload the parent
        if (descriptor.getOwner().startsWith("[")) {
            return arrayClass.getMethod(descriptor.getName(), descriptor.getDesc());
        }

        findClass(descriptor.getOwner());
        return functionEquivalence.get(descriptor);
    }

    @Override
    public FieldNode findField(ClassDescriptor descriptor) {
        // Hotload the parent
        findClass(descriptor.getOwner());

        return fieldEquivalence.get(descriptor);
    }

    public KlassNode create(final ClassNode classNode) {
        KlassNode klassNode = classEquivalence.get(classNode.name);

        if (klassNode != null && !(klassNode instanceof UnresolvedKlassNode)) {
            throw new IllegalStateException(String.format(
                    "Attempting to recreate resolved class %s",
                    classNode.name
            ));
        }

        klassNode = new ResolvedKlassNode(this, classNode);
        classEquivalence.put(classNode.name, klassNode);
        klassGraph.addVertex(klassNode);

        for (org.objectweb.asm.tree.FieldNode field : classNode.fields) {
            final FieldNode fieldNode = create(klassNode, field);
            klassNode.addField(fieldNode);
        }

        return klassNode;
    }

    @Override
    public void addMethod(FunctionNode node) {
        final ClassDescriptor descriptor = new ClassDescriptor(
                node.getOwner().getName(),
                node.getOriginalDescriptor().getName(),
                node.getOriginalDescriptor().getDesc()
        );

        if (functionEquivalence.get(descriptor) == null) {
            functionEquivalence.put(descriptor, node);
            functionGraph.addVertex(node);
        } else {
            throw new IllegalStateException("Method already exists: " + descriptor);
        }
    }

    public FieldNode create(final KlassNode parent, final org.objectweb.asm.tree.FieldNode node) {
        final ClassDescriptor descriptor = new ClassDescriptor(
                parent.getName(),
                node.name,
                node.desc
        );

        FieldNode fieldNode = findField(descriptor);

        if (fieldNode == null) {
            fieldNode = new ResolvedFieldNode(
                    this,
                    node
            );
            fieldNode.setParent(parent);

            fieldEquivalence.put(descriptor, fieldNode);
        } else {
            throw new IllegalStateException("Field already exists: " + descriptor);
        }

        return fieldNode;
    }

    public void resolveKlassEdges(final KlassNode klassNode) {
        //System.out.println("Previous edges: " + klassGraph.outgoingEdgesOf(klassNode));
        for (KlassInheritanceEdge edge : new HashSet<>(klassGraph.outgoingEdgesOf(klassNode))) {
            klassGraph.removeEdge(edge);
        }
        //System.out.println("Omitted previous edges: " + klassGraph.outgoingEdgesOf(klassNode));

        if (klassNode.getParent() != null) {
            //System.out.println("Adding edge: " + klassNode + " -> " + klassNode.getParent());
            final boolean success = klassGraph.addEdge(
                    klassNode,
                    klassNode.getParent(),
                    new KlassExtendsEdge(klassNode, klassNode.getParent())
            );
        }

        for (KlassNode interfaze : klassNode.getInterfaces()) {
            //System.out.println("Adding edge: " + klassNode + " -> " + klassNode.getParent());
            final boolean success = klassGraph.addEdge(
                    klassNode,
                    interfaze,
                    new KlassImplementsEdge(klassNode, klassNode.getParent())
            );
        }
    }

}
