package dev.skidfuscator.ir.exception;

public abstract class SkidfuscatorIrException extends RuntimeException {

    public SkidfuscatorIrException() {
    }

    public SkidfuscatorIrException(String message) {
        super(message);
    }

    public SkidfuscatorIrException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkidfuscatorIrException(Throwable cause) {
        super(cause);
    }

    public SkidfuscatorIrException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
