package dev.skidfuscator.ir.insn;

//import dev.skidfuscator.ir.gen.BytecodeFrontend;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import org.objectweb.asm.MethodVisitor;

public abstract class AbstractInsn implements Insn {
    protected final Hierarchy hierarchy;

    public AbstractInsn(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }
}
