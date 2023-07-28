package dev.skidfuscator.ir.hierarchy.impl;

import dev.skidfuscator.ir.hierarchy.HierarchyConfig;
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
import org.jgrapht.nio.GraphExporter;
import org.jgrapht.nio.dot.DOTExporter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SkidHierarchy implements Hierarchy {
    private final HierarchyConfig config;

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
    protected final Map<String, KlassNode> programClassEquivalence;
    private final KlassGraph klassGraph;
    private final KlassNode rootClass;
    private final KlassNode arrayClass;
    private final KlassNode primitiveArrayClass;


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
        this(HierarchyConfig.Builder
                .of()
                .generatePhantomMethods()
                .generatePhantomClasses()
                .generatePhantomFields()
                .build()
        );
    }

    public SkidHierarchy(final HierarchyConfig config) {
        this.config = config;
        this.classEquivalence = new HashMap<>();
        this.programClassEquivalence = new HashMap<>();
        this.functionEquivalence = new HashMap<>();
        this.fieldEquivalence = new HashMap<>();
        this.klassGraph = new KlassGraph();
        this.functionGraph = new FunctionGraph();

        this.rootClass = this.create(ClassHelper.create(Object.class), false);
        this.arrayClass = new ArraySpecialKlassNode(this, 1, rootClass);
        this.primitiveArrayClass = new ArraySpecialKlassNode(this, 0, rootClass);
    }

    @Override
    public HierarchyConfig getConfig() {
        return config;
    }

    @Override
    public Iterable<KlassNode> iterateKlasses() {
        return Collections.unmodifiableCollection(programClassEquivalence.values());
    }

    @Override
    public void resolveClasses(final Collection<ClassNode> classes) {
        classes.forEach(this::create);
        new HashSet<>(classEquivalence.values())
                .stream()
                .filter(e -> !e.isResolvedHierarchy())
                .forEach(KlassNode::resolveHierarchy);

        classes.stream()
                .map(e -> e.name)
                .map(classEquivalence::get)
                .forEach(e -> {
                    programClassEquivalence.put(e.getName(), e);
                });

        final Set<KlassNode> resolved = new HashSet<>();

        final KlassNode root = findClass("java/lang/Object");
        final Queue<KlassNode> queue = new LinkedBlockingQueue<>();
        queue.add(root);

        /*final File file = new File("graph.dot");
        System.out.println("Exporting graph to " + file.getAbsolutePath());
        try {
            application.stream().map(e -> classEquivalence.get(e)).forEach(e -> {
                resolveKlassEdges(e);
            });
            final DOTExporter<KlassNode, KlassInheritanceEdge> exporter = new DOTExporter<>(
                    new Function<KlassNode, String>() {
                        @Override
                        public String apply(KlassNode klassNode) {
                            return klassNode.getName()
                                    .replace("/", "_")
                                    .replace("$", "__")
                                    .replace("-", "___");
                        }
                    }
            );
            exporter.exportGraph(klassGraph, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        for (KlassNode node : programClassEquivalence.values()) {
            if (!node.isResolvedInternal())
                node.resolveInternal();
        }

        for (KlassNode node : programClassEquivalence.values()) {
            node.resolveInstructions();
        }

        /*while (!queue.isEmpty()) {
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
        }*/
    }

    @Override
    public KlassNode findClass(String name) {
        // Hotload the parent
        final KlassNode node = classEquivalence.get(name);

        //System.out.println("Finding " + name + " " + node);
        if (name.startsWith("[")) {
            final String baseName = name.substring(name.lastIndexOf("[") + 1);
            final Type type = Type.getType(name).getElementType();
            final String parsedName = type.getClassName().replace(".", "/");

            //System.out.println("Base name " + parsedName + " for " + name + "");
            KlassNode arrayClass = findClass(parsedName);

            if (arrayClass == null) {
                return type.getSort() != Type.OBJECT ? this.primitiveArrayClass : this.arrayClass;
            }

            final int dimensions = name.lastIndexOf("[") + 1;

            arrayClass = new ArraySpecialKlassNode(
                    this,
                    dimensions,
                    arrayClass
            );
            classEquivalence.put(name, arrayClass);

            return arrayClass;
        }

        return node;
    }

    @Override
    public KlassNode resolveClass(String name) {
        KlassNode node = findClass(name);

        if (node != null) {
            return node;
        }

        System.err.println("WARN! Unresolved " + name);
        node = new UnresolvedKlassNode(this, rootClass, name);
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
        return create(classNode, true);
    }

    public KlassNode create(final ClassNode classNode, final boolean strict) {
        KlassNode klassNode = classEquivalence.get(classNode.name);

        if (klassNode != null && !(klassNode instanceof UnresolvedKlassNode)) {
            throw new IllegalStateException(String.format(
                    "Attempting to recreate resolved class %s",
                    classNode.name
            ));
        }

        klassNode = new ResolvedKlassNode(this, classNode, strict);
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
