package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TypeInsn extends AbstractInsn<TypeInsnNode> {
    private TypeWrapper target;

    public TypeInsn(Hierarchy hierarchy, TypeInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        String desc = node.desc;
        System.out.printf("TypeInsn: %s\n", desc);
        switch (node.desc) {
            case "I":
            case "Z":
            case "B":
            case "C":
            case "S":
            case "F":
            case "J":
            case "D":
            case "V":
                break;
            default:
                if (desc.contains("[")) {
                    break;
                }

                desc = "L" + desc + ";";
        }
        this.target = new TypeWrapper(Type.getType(desc), hierarchy);
        this.target.resolveHierarchy();

        super.resolve();
    }

    @Override
    public TypeInsnNode dump() {
        this.node.desc = /*"L" +*/ target.dump().getInternalName() /*+ ";"*/;
        return super.dump();
    }

    @Override
    public String toString() {
        final String name = target.dump().getInternalName();
        return switch (node.getOpcode()) {
            case Opcodes.NEW -> "new " + name;
            case Opcodes.ANEWARRAY -> "anewarray " + name;
            case Opcodes.CHECKCAST -> "checkcast " + name;
            case Opcodes.INSTANCEOF -> "instanceof " + name;
            default -> "unknown type " + name;
        };
    }
}
