package dev.skidfuscator.ir.asm;

import dev.skidfuscator.ir.asm.util.AsmUtil;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.impl.AbstractInstructionList;
import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.klass.KlassTags;
import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.method.MethodTags;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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

        final Method method = Method.of()
                .owner(visitor)
                .name(name)
                .args(argsKlass)
                .returnType(returnKlass)
                .instructions(new AbstractInstructionList(new ArrayList<>()))
                // TODO: group handling via hierarchy
                .tags(resolveMethodTags(access))
                .build();

        return new Asm2SkidMethodNodeVisitor(
                hierarchy,
                method,
                super.visitMethod(access, name, descriptor, signature, exceptions)
        );
    }

    private static final Map<Integer, KlassTags> klassTags;
    private static final Map<Integer, MethodTags> methodTags;

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
