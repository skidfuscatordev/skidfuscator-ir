package dev.skidfuscator.ir.insn;

public class IllegalInstructionException extends IllegalStateException {
    public IllegalInstructionException(String var, Object... values) {
        super(String.format(var, values));
    }
}
