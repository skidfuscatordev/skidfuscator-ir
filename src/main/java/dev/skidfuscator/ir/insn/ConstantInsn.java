package dev.skidfuscator.ir.insn;

import dev.skidfuscator.ir.hierarchy.Hierarchy;

public abstract class ConstantInsn extends AbstractInsn {
    protected Object constant;

    public ConstantInsn(Hierarchy hierarchy) {
        super(hierarchy);
    }

    public void setConstant(final Object constant) {
        this.constant = constant;
    }

    public Object getConstant() {
        return constant;
    }
}
