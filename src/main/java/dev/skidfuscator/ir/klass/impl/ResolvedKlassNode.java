package dev.skidfuscator.ir.klass.impl;

import dev.skidfuscator.ir.field.impl.ResolvedFieldNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.annotation.Annotation;
import dev.skidfuscator.ir.field.FieldNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.impl.ResolvedMutableFunctionNode;
import dev.skidfuscator.ir.record.RecordComponent;
import dev.skidfuscator.ir.signature.SignatureWrapper;
import dev.skidfuscator.ir.type.TypeWrapper;
import dev.skidfuscator.ir.util.Descriptor;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.RecordComponentNode;

import java.util.*;

public class ResolvedKlassNode implements KlassNode {
    private final Hierarchy hierarchy;
    private final ClassNode node;
    private boolean mutable;
    private boolean resolvedInternally;
    private boolean resolvedHierarchy;

    private KlassNode parent;
    private List<KlassNode> interfaces;
    private List<Annotation> annotations;
    private String name;

    private SignatureWrapper signatureWrapper;

    private int access;
    private Map<Descriptor, FunctionNode> methods;
    private Map<Descriptor, FieldNode> fields;
    private final boolean strict;

    private List<KlassNode> permittedSubclasses;
    private List<RecordComponent> recordComponents;
    private List<KlassNode> innerClasses; //TODO
    private List<KlassNode> nestMembers;

    private KlassNode nestHost;
    private KlassNode outerClass;
    private TypeWrapper outerMethodDesc;

    public ResolvedKlassNode(Hierarchy hierarchy, ClassNode node, boolean strict) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.interfaces = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();
        this.name = node.name;
        this.signatureWrapper = new SignatureWrapper(node.signature, hierarchy);
        this.access = node.access;
        this.mutable = true;
        this.strict = strict;
    }

    @Override
    public boolean isResolvedHierarchy() {
        return resolvedHierarchy;
    }

    @Override
    public boolean isResolvedInternal() {
        return resolvedInternally;
    }

    @Override
    public void resolveHierarchy() {
        if (node.superName != null) {
            this.parent = hierarchy.resolveClass(node.superName);
        }

        if (!this.signatureWrapper.isResolved()) {
            this.signatureWrapper.resolveHierarchy();
        }

        if (node.interfaces != null) {
            for (String interfaze : node.interfaces) {
                final KlassNode resolved = hierarchy.resolveClass(interfaze);
                this.interfaces.add(resolved);
            }
        }

        /*
         * Resolve the methods and overwrite any parent
         * method which is being added.
         */
        for (MethodNode method : this.node.methods) {
            //System.out.println("\\-> " + method.name + method.desc);
            final FunctionNode function = new ResolvedMutableFunctionNode(
                    hierarchy,
                    Descriptor.of(
                            method.name,
                            method.desc
                    ),
                    method,
                    new HashSet<>()
            );
            function.setOwner(this);
        }

        //System.out.println("Finished resolving " + this.name + " methods");
        for (FunctionNode method : this.methods.values()) {
            this.hierarchy.addMethod(method);
        }
        //System.out.println("Finished adding " + this.name + " methods");
        this.hierarchy.resolveKlassEdges(this);
        this.resolvedHierarchy = true;
    }

    @Override
    public void lock() {
        this.mutable = false;
        this.methods.values().forEach(FunctionNode::lock);
        this.fields.values().forEach(FieldNode::lock);
    }

    @Override
    public void resolveInternal() {
        assert !resolvedInternally : String.format("Class %s is already resolved!", this.name);
        if (parent != null) {
            if (!parent.isResolvedHierarchy())
                parent.resolveHierarchy();

            if (!parent.isResolvedInternal())
                parent.resolveInternal();
        }

        for (KlassNode impl : this.interfaces) {
            if (!impl.isResolvedInternal())
                impl.resolveInternal();
        }

        //System.out.println("Trying to find parent " + this.node.superName + " for " + this.name);

        if (this.node.superName != null && parent == null) {
            throw new IllegalStateException(String.format(
                    "Class %s has a parent %s which is not resolved!",
                    this.name,
                    this.node.superName
            ));
        }

        if (this.node.permittedSubclasses != null) {
            for (String permittedSubclass : this.node.permittedSubclasses) {
                this.permittedSubclasses.add(hierarchy.resolveClass(permittedSubclass));
            }
        }

        if (this.node.nestMembers != null) {
            for (String nestMember : this.node.nestMembers) {
                this.nestMembers.add(hierarchy.resolveClass(nestMember));
            }
        }

        if (this.node.nestHostClass != null) {
            this.nestHost = hierarchy.resolveClass(this.node.nestHostClass);
        }

        if (this.node.outerClass != null && this.node.outerMethodDesc != null) {
            this.outerClass = hierarchy.resolveClass(this.node.outerClass);
            this.outerMethodDesc = new TypeWrapper(Type.getType(this.node.outerMethodDesc), hierarchy);
            if (!this.outerMethodDesc.isResolved())
                this.outerMethodDesc.resolveHierarchy();
        }

        /*
         * Resolve the fields and overwrite any parent
         * field which is being added.
         */
        if (parent != null) {
            //System.out.println("\n-----------\nResolving parent " + parent.getName() + " fields for " + this.name);
            for (final FunctionNode parentFunction : this.parent.getMethods()) {
                inheritFunction(parentFunction);
            }
            //System.out.println("Success! Resolved parent " + parent.getName() + " fields for " + this.name);
        }


        //System.out.println("Resolving " + this.name + " implementations");
        for (KlassNode implementation : this.interfaces) {
            //System.out.println("Resolving implementation " + implementation.getName() + " fields for " + this.name);
            for (final FunctionNode parentFunction : implementation.getMethods()) {
                inheritFunction(parentFunction);
            }
        }

        this.resolvedInternally = true;

        for (FunctionNode method : this.methods.values()) {
            //System.out.printf("Resolving %s\n", method);
            method.resolveHierarchy();
        }

        for (FieldNode field : this.fields.values()) {
            field.resolveHierachy();
        }

        for (RecordComponentNode recordComponent : this.node.recordComponents) {
            this.recordComponents.add(new RecordComponent(recordComponent, hierarchy, strict));
        }

        //System.out.println("Success! Resolved " + this.name + " implementations");

        if (node.visibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.VISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.visibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_VISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.invisibleAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.INVISIBLE);
                this.annotations.add(annotation);
            }
        }

        if (node.invisibleTypeAnnotations != null) {
            for (AnnotationNode annotationNode : node.invisibleTypeAnnotations) {
                final Annotation annotation = new Annotation(hierarchy, annotationNode, Annotation.AnnotationType.TYPE_INVISIBLE);
                this.annotations.add(annotation);
            }
        }

        for (Annotation annotation : this.annotations) {
            try {
                annotation.resolve();
            } catch (IllegalStateException e) {
                if (strict) {
                    throw new IllegalStateException(
                            String.format("Failed to resolve annotation %s on %s", annotation.getNode().desc, this.name),
                            e
                    );
                } else {
                    System.err.println(
                            String.format("Failed to resolve annotation %s on %s", annotation.getNode().desc, this.name)
                    );
                    e.printStackTrace();
                }

            }
        }
    }

    private void inheritFunction(final FunctionNode parentFunction) {
        // Don't inherit constructors or class initializers
        if (parentFunction.isConstructor() || parentFunction.isClassInit())
            return;

        final FunctionNode childFunction = this.methods.get(parentFunction.getOriginalDescriptor());

        // Don't inherit private functions or functions that have already been inherited
        if (parentFunction.isPrivate()) {
            return;
        }

        if (childFunction != null) {
            // Don't inherit static functions which are defined
            // in the child class
            if (childFunction.isStatic()) {
                return;
            }

            // Inherit the function if it is not private
            childFunction.addParent(parentFunction);
            return;
        }

        // Inherit the function if it is not private
        // and it is not defined in the child class
        // as a wrapper synthetic function
        final FunctionNode function = new ResolvedMutableFunctionNode(
                hierarchy,
                parentFunction.getOriginalDescriptor(),
                null,
                new HashSet<>()
        );
        function.setOwner(this);
        hierarchy.addMethod(function);
    }

    @Override
    public void resolveInstructions() {
        /*
         * Since the classes are being resolved in
         * a BFS manner, the method groups will be
         * fine.
         */
        for (FunctionNode method : this.methods.values()) {
            method.resolveInternal();
        }
    }

    @Override
    public @NotNull Type asType() {
        return Type.getObjectType(this.getName());
    }

    @Override
    public @NotNull Collection<FunctionNode> getMethods() {
        return this.methods == null
                ? Collections.emptySet()
                : Collections.unmodifiableCollection(methods.values());
    }

    @Override
    public @NotNull FunctionNode getMethod(String name, String desc) {
        final Descriptor descriptor = new Descriptor(name, desc);
        final FunctionNode node = this.methods.get(descriptor);

        if (node != null) {
            return node;
        }

        if (strict) {
            throw new IllegalStateException(
                    String.format("Method %s%s does not exist in class %s", name, desc, this.name)
            );
        }

        final FunctionNode functionNode = new ResolvedMutableFunctionNode(
                hierarchy,
                descriptor,
                null,
                null
        );
        functionNode.setOwner(this);
        functionNode.resolveHierarchy();
        hierarchy.addMethod(functionNode);

        return this.methods.get(descriptor);
    }

    @Override
    public @NotNull Collection<FieldNode> getFields() {
        return this.fields == null
                ? Collections.emptyList()
                : Collections.unmodifiableCollection(fields.values());
    }

    @Override
    public FieldNode getField(String name, String desc) {
        final Descriptor descriptor = new Descriptor(name, desc);
        FieldNode node = this.fields.get(descriptor);

        if (node != null) {
            return node;
        }

        if (parent != null) {
            node = parent.getField(name, desc);
            if (node != null) {
                return node;
            }
        }

        if (strict) {
            throw new IllegalStateException(
                    String.format("Field %s%s does not exist in class %s", name, desc, this.name)
            );
        }

        if (hierarchy.getConfig().isGeneratePhantomFields()) {
            final FieldNode fieldNode = new ResolvedFieldNode(
                    hierarchy,
                    new org.objectweb.asm.tree.FieldNode(
                            Opcodes.ACC_PUBLIC,
                            name,
                            desc,
                            null,
                            null
                    )
            );
            fieldNode.setParent(this);
            this.fields.put(descriptor, fieldNode);
        }

        return this.fields.get(descriptor);
    }

    @Override
    public void addMethod(FunctionNode node) {
        if (!mutable)
            throw new IllegalStateException("Cannot add methods on a locked class");

        assert node.getOwner() != null : "Cannot add a method that does not a parent. Please re-assign in the function node itself by calling FunctionNode#setParent(KlassNode)";
        this.addMethod0(node);
    }

    private void addMethod0(final FunctionNode node) {
        this.methods.put(node.getOriginalDescriptor(), node);
    }

    @Override
    public void removeMethod(FunctionNode node) {
        if (!mutable)
            throw new IllegalStateException("Cannot remove methods on a locked class");

        assert node.getOwner() == this : "Cannot remove a method from a class that is not its parent";
        this.methods.remove(node.getOriginalDescriptor());
    }

    @Override
    public void addField(FieldNode node) {
        if (!mutable)
            throw new IllegalStateException("Cannot add fields on a locked class");

        assert node.getParent() != null : "Cannot add a field that has a parent. Please re-assign in the field node itself by calling FieldNode#setParent(KlassNode)";

        node.setParent(this);
        this.fields.put(Descriptor.of(node), node);
    }

    @Override
    public void removeField(FieldNode node) {
        if (!mutable)
            throw new IllegalStateException("Cannot remove fields on a locked class");

        assert node.getParent() == this : "Cannot remove a field from a class that is not its parent";
        this.fields.remove(node);
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public void setName(final @NotNull String name) {
        if (!mutable)
            throw new IllegalStateException("Cannot set name on a locked class");

        this.name = name;
    }

    @Override
    public String getSignature() {
        return signatureWrapper.isResolved() ? signatureWrapper.dump() : node.signature;
    }

    @Override
    public KlassNode getParent() {
        return parent;
    }

    @Override
    public void setParent(KlassNode klass) {
        if (!mutable)
            throw new IllegalStateException("Cannot set parent on a locked class");

        this.parent = klass;

        // Update hierarchy
        this.hierarchy.resolveKlassEdges(this);
    }

    @Override
    public @NotNull List<KlassNode> getInterfaces() {
        return interfaces == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(interfaces);
    }

    @Override
    public void setInterfaces(List<KlassNode> klasses) {
        if (!mutable)
            throw new IllegalStateException("Cannot set interfaces on a locked class");

        this.interfaces = klasses;

        // Update hierarchy
        this.hierarchy.resolveKlassEdges(this);
    }

    @Override
    public boolean isInterface() {
        return (this.access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public void dump() {
        this.node.name = name;
        this.node.signature = getSignature();
        this.node.access = access;

        this.node.superName = parent == null ? null : parent.getName();
        this.node.nestHostClass = this.nestHost.getName();
        this.node.outerClass = this.outerClass.getName();
        this.node.outerMethodDesc = this.outerMethodDesc.dump().getDescriptor();

        this.node.interfaces = new ArrayList<>();
        for (KlassNode impl : this.interfaces) {
            this.node.interfaces.add(impl.getName());
        }

        this.node.fields.clear();
        for (FieldNode field : fields.values()) {
            this.node.fields.add(field.dump());
        }

        this.node.methods.clear();
        for (FunctionNode method : methods.values()) {
            if (method.isSynthetic())
                continue;

            this.node.methods.add(method.dump());
        }

        this.node.permittedSubclasses = new ArrayList<>();
        for (KlassNode permittedSubclass : this.permittedSubclasses) {
            this.node.permittedSubclasses.add(permittedSubclass.getName());
        }

        this.node.nestMembers = new ArrayList<>();
        for (KlassNode nestMember : this.nestMembers) {
            this.node.nestMembers.add(nestMember.getName());
        }

        for (RecordComponent recordComponent : this.recordComponents) {
            recordComponent.dump();
        }

        for (Annotation annotation : this.annotations) {
            annotation.dump();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
