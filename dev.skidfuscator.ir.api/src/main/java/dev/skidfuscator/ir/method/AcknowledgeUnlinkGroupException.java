package dev.skidfuscator.ir.method;

import java.io.IOException;

public class AcknowledgeUnlinkGroupException extends IOException {
    private AcknowledgeUnlinkGroupException() {
        super("Please catch this exception to acknowledge the group unlink. " +
                "This means hierarchy for this method will no longer be resolved with the group, which may cause issues.");
    }

    public static AcknowledgeUnlinkGroupException check() throws AcknowledgeUnlinkGroupException {
        throw new AcknowledgeUnlinkGroupException();
    }
}
