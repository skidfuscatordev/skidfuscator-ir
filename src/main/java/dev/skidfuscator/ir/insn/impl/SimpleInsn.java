package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import dev.skidfuscator.ir.insn.Insn;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

public class SimpleInsn extends AbstractInsn<InsnNode> {
    public SimpleInsn(Hierarchy hierarchy, InsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public String toString() {
        switch (node.getOpcode()) {
            case Opcodes.NOP: return "nop";
            case Opcodes.ACONST_NULL: return "push(smple) aconst_null";
            case Opcodes.ICONST_M1: return "push(smple) iconst_m1";
            case Opcodes.ICONST_0: return "push(smple) iconst_0";
            case Opcodes.ICONST_1: return "push(smple) iconst_1";
            case Opcodes.ICONST_2: return "push(smple) iconst_2";
            case Opcodes.ICONST_3: return "push(smple) iconst_3";
            case Opcodes.ICONST_4: return "push(smple) iconst_4";
            case Opcodes.ICONST_5: return "push(smple) iconst_5";
            case Opcodes.LCONST_0: return "push(smple) lconst_0";
            case Opcodes.LCONST_1: return "push(smple) lconst_1";
            case Opcodes.FCONST_0: return "push(smple) fconst_0";
            case Opcodes.FCONST_1: return "push(smple) fconst_1";
            case Opcodes.FCONST_2: return "push(smple) fconst_2";
            case Opcodes.DCONST_0: return "push(smple) dconst_0";
            case Opcodes.DCONST_1: return "push(smple) dconst_1";
            case Opcodes.IALOAD: return "push(smpl) iaload";
            case Opcodes.LALOAD: return "push(smpl) laload";
            case Opcodes.FALOAD: return "push(smpl) faload";
            case Opcodes.DALOAD: return "push(smpl) daload";
            case Opcodes.AALOAD: return "push(smpl) aaload";
            case Opcodes.BALOAD: return "push(smpl) baload";
            case Opcodes.CALOAD: return "push(smpl) caload";
            case Opcodes.SALOAD: return "push(smpl) saload";
            case Opcodes.IASTORE: return "pop(smpl) iastore";
            case Opcodes.LASTORE: return "pop(smpl) lastore";
            case Opcodes.FASTORE: return "pop(smpl) fastore";
            case Opcodes.DASTORE: return "pop(smpl) dastore";
            case Opcodes.AASTORE: return "pop(smpl) aastore";
            case Opcodes.BASTORE: return "pop(smpl) bastore";
            case Opcodes.CASTORE: return "pop(smpl) castore";
            case Opcodes.SASTORE: return "pop(smpl) sastore";
            case Opcodes.POP: return "pop(smpl)";
            case Opcodes.POP2: return "pop2(smpl)";
            case Opcodes.DUP: return "push(smpl) dup";
            case Opcodes.DUP_X1: return "push(smpl) dup_x1";
            case Opcodes.DUP_X2: return "push(smpl) dup_x2";
            case Opcodes.DUP2: return "push(smpl) dup2";
            case Opcodes.DUP2_X1: return "push(smpl) dup2_x1";
            case Opcodes.DUP2_X2: return "push(smpl) dup2_x2";
            case Opcodes.SWAP: return "swap(smpl)";
            case Opcodes.IADD: return "iadd";
            case Opcodes.LADD: return "ladd";
            case Opcodes.FADD: return "fadd";
            case Opcodes.DADD: return "dadd";
            case Opcodes.ISUB: return "isub";
            case Opcodes.LSUB: return "lsub";
            case Opcodes.FSUB: return "fsub";
            case Opcodes.DSUB: return "dsub";
            case Opcodes.IMUL: return "imul";
            case Opcodes.LMUL: return "lmul";
            case Opcodes.FMUL: return "fmul";
            case Opcodes.DMUL: return "dmul";
            case Opcodes.IDIV: return "idiv";
            case Opcodes.LDIV: return "ldiv";
            case Opcodes.FDIV: return "fdiv";
            case Opcodes.DDIV: return "ddiv";
            case Opcodes.IREM: return "irem";
            case Opcodes.LREM: return "lrem";
            case Opcodes.FREM: return "frem";
            case Opcodes.DREM: return "drem";
            case Opcodes.INEG: return "ineg";
            case Opcodes.LNEG: return "lneg";
            case Opcodes.FNEG: return "fneg";
            case Opcodes.DNEG: return "dneg";
            case Opcodes.ISHL: return "ishl";
            case Opcodes.LSHL: return "lshl";
            case Opcodes.ISHR: return "ishr";
            case Opcodes.LSHR: return "lshr";
            case Opcodes.IUSHR: return "iushr";
            case Opcodes.LUSHR: return "lushr";
            case Opcodes.IAND: return "iand";
            case Opcodes.LAND: return "land";
            case Opcodes.IOR: return "ior";
            case Opcodes.LOR: return "lor";
            case Opcodes.IXOR: return "ixor";
            case Opcodes.LXOR: return "lxor";
            case Opcodes.I2L: return "i2l";
            case Opcodes.I2F: return "i2f";
            case Opcodes.I2D: return "i2d";
            case Opcodes.L2I: return "l2i";
            case Opcodes.L2F: return "l2f";
            case Opcodes.L2D: return "l2d";
            case Opcodes.F2I: return "f2i";
            case Opcodes.F2L: return "f2l";
            case Opcodes.F2D: return "f2d";
            case Opcodes.D2I: return "d2i";
            case Opcodes.D2L: return "d2l";
            case Opcodes.D2F: return "d2f";
            case Opcodes.I2B: return "i2b";
            case Opcodes.I2C: return "i2c";
            case Opcodes.I2S: return "i2s";
            case Opcodes.LCMP: return "lcmp";
            case Opcodes.FCMPL: return "fcmpl";
            case Opcodes.FCMPG: return "fcmpg";
            case Opcodes.DCMPL: return "dcmpl";
            case Opcodes.DCMPG: return "dcmpg";
            case Opcodes.ATHROW: return "athrow";
            case Opcodes.MONITORENTER: return "monitorenter";
            case Opcodes.MONITOREXIT: return "monitorexit";
        }

        return "unknown " + node.getOpcode();
    }
}
