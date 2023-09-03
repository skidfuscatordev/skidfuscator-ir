package dev.skidfuscator.ir.insn.exception;

import dev.skidfuscator.ir.exception.SkidfuscatorIrException;

public class IllegalInstructionException extends SkidfuscatorIrException {
    public IllegalInstructionException(String message, Object... objects) {
        super(message.formatted(objects));
    }
}
