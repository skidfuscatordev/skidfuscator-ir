package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.klass.KlassNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TypeInsn extends AbstractInsn<TypeInsnNode> {
    private KlassNode target;

    public TypeInsn(Hierarchy hierarchy, TypeInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        switch (node.desc) {
            case "I" -> this.target = hierarchy.findClass("I");
            case "J" -> this.target = hierarchy.findClass("J");
            case "F" -> this.target = hierarchy.findClass("F");
            case "D" -> this.target = hierarchy.findClass("D");
            case "B" -> this.target = hierarchy.findClass("B");
            case "S" -> this.target = hierarchy.findClass("S");
            case "C" -> this.target = hierarchy.findClass("C");
            case "Z" -> this.target = hierarchy.findClass("Z");
            case "V" -> this.target = hierarchy.findClass("V");
            default -> {
                if (node.desc.startsWith("[")) {
                    this.target = hierarchy.resolveClass(node.desc);
                } else {
                    this.target = hierarchy.resolveClass(Type.getObjectType(node.desc).getInternalName());
                }
            }
        }

        if (this.target == null) {
            throw new IllegalStateException(String.format(
                    "Could not find class for type instruction %s",
                    node.desc
            ));
        }

        super.resolve();
    }

    @Override
    public TypeInsnNode dump() {
        //this.node.desc = /*"L" +*/ target.getName() /*+ ";"*/;
        return super.dump();
    }

    @Override
    public String toString() {
        return switch (node.getOpcode()) {
            case Opcodes.NEW -> "new " + target.getName();
            case Opcodes.ANEWARRAY -> "anewarray " + target.getName();
            case Opcodes.CHECKCAST -> "checkcast " + target.getName();
            case Opcodes.INSTANCEOF -> "instanceof " + target.getName();
            default -> "unknown type " + target.getName();
        };
    }
}
