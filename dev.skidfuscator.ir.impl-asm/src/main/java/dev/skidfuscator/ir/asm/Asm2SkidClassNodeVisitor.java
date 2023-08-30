package dev.skidfuscator.ir.asm;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.asm.util.AsmUtil;
import dev.skidfuscator.ir.field.FieldTags;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.impl.AbstractInstructionList;
import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.klass.KlassTags;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.method.MethodTags;
import org.objectweb.asm.*;

import java.util.*;

public class Asm2SkidClassNodeVisitor extends ClassVisitor {
    private final Hierarchy hierarchy;
    private final Klass visitor;

    public Asm2SkidClassNodeVisitor(Hierarchy hierarchy, Klass visitor) {
        super(AsmUtil.ASM_VERSION);
        this.hierarchy = hierarchy;
        this.visitor = visitor;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        final Klass parent = hierarchy.resolveClass(superName);
        final List<Klass> implement = new ArrayList<>();

        for (String anInterface : interfaces) {
            implement.add(hierarchy.resolveClass(anInterface));
        }

        visitor.visit(
                name,
                parent,
                implement,
                resolveKlassTags(access),
                signature
        );

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final Type methodType = Type.getMethodType(descriptor);

        final List<Klass> argsKlass = new ArrayList<>();
        for (Type argumentType : methodType.getArgumentTypes()) {
            argsKlass.add(hierarchy.resolveClass(argumentType.getInternalName()));
        }

        final Klass returnKlass = hierarchy.resolveClass(
                methodType.getReturnType().getInternalName()
        );

        final dev.skidfuscator.ir.method.MethodVisitor method = visitor.visitMethod();
        method.visit(visitor, name, resolveMethodTags(access), argsKlass, returnKlass);

        return new Asm2SkidMethodNodeVisitor(
                hierarchy,
                method,
                super.visitMethod(access, name, descriptor, signature, exceptions)
        );
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        final Type fieldType = Type.getType(descriptor);
        final Klass fieldKlass = hierarchy.resolveClass(fieldType.getInternalName());

        final Field field = Field.of()
                .name(name)
                .tags(resolveFieldTags(access))
                .owner(visitor)
                .type(fieldKlass)
                .dflt(value)
                .build();

        return new Asm2SkidFieldNodeVisitor(
                hierarchy,
                field,
                super.visitField(access, name, descriptor, signature, value)
        );
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        return super.visitModule(name, access, version);
    }

    @Override
    public void visitNestHost(String nestHost) {
        super.visitNestHost(nestHost);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    @Override
    public void visitNestMember(String nestMember) {
        super.visitNestMember(nestMember);
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        super.visitPermittedSubclass(permittedSubclass);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return super.visitRecordComponent(name, descriptor, signature);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private static final Map<Integer, KlassTags> klassTags;
    private static final Map<Integer, MethodTags> methodTags;
    private static final Map<Integer, FieldTags> fieldTags;

    static {
        klassTags = Map.of(
            Opcodes.ACC_PRIVATE, KlassTags.PRIVATE,
            Opcodes.ACC_PROTECTED, KlassTags.PROTECTED,
            Opcodes.ACC_PUBLIC, KlassTags.PRIVATE,
            Opcodes.ACC_FINAL, KlassTags.FINAL
            // temp, waiting for narumii fix
        );

        methodTags = Map.of(
                Opcodes.ACC_PRIVATE, MethodTags.PRIVATE,
                Opcodes.ACC_PROTECTED, MethodTags.PROTECTED,
                Opcodes.ACC_PUBLIC, MethodTags.PRIVATE,
                Opcodes.ACC_FINAL, MethodTags.FINAL
                // temp, waiting for narumii fix
        );

        fieldTags = Map.of(
                Opcodes.ACC_PRIVATE, FieldTags.PRIVATE,
                Opcodes.ACC_PROTECTED, FieldTags.PROTECTED,
                Opcodes.ACC_PUBLIC, FieldTags.PRIVATE,
                Opcodes.ACC_FINAL, FieldTags.FINAL,
                Opcodes.ACC_TRANSIENT, FieldTags.TRANSIENT
                // temp, waiting for narumii fix
        );
    }

    private Set<FieldTags> resolveFieldTags(final int tag) {
        final Set<FieldTags> tags = new HashSet<>();

        fieldTags.forEach((acc, resolve) -> {
            if ((tag & acc) != 0) {
                tags.add(resolve);
            }
        });

        return tags;
    }

    private Set<MethodTags> resolveMethodTags(final int tag) {
        final Set<MethodTags> tags = new HashSet<>();

        methodTags.forEach((acc, resolve) -> {
            if ((tag & acc) != 0) {
                tags.add(resolve);
            }
        });

        return tags;
    }

    private Set<KlassTags> resolveKlassTags(final int tag) {
        final Set<KlassTags> tags = new HashSet<>();

        klassTags.forEach((acc, resolve) -> {
            if ((tag & acc) != 0) {
                tags.add(resolve);
            }
        });

        return tags;
    }
}
