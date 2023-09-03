package dev.skidfuscator.ir.access;

public enum Access {

    PUBLIC(0x0001, Target.CLASS, Target.FIELD, Target.METHOD),
    PRIVATE(0x0002, Target.CLASS, Target.FIELD, Target.METHOD),
    PROTECTED(0x0004, Target.CLASS, Target.FIELD, Target.METHOD),
    STATIC(0x0008, Target.FIELD, Target.METHOD),
    FINAL(0x0010, Target.CLASS, Target.FIELD, Target.METHOD, Target.PARAMETER),
    SUPER(0x0020, Target.CLASS),
    SYNCHRONIZED(0x0020, Target.METHOD),
    OPEN(0x0020, Target.MODULE),
    TRANSITIVE(0x0020, Target.MODULE_REQUIRES),
    VOLATILE(0x0040, Target.FIELD),
    BRIDGE(0x0040, Target.METHOD),
    STATIC_PHASE(0x0040, Target.MODULE_REQUIRES),
    VARARGS(0x0080, Target.METHOD),
    TRANSIENT(0x0080, Target.FIELD),
    NATIVE(0x0100, Target.METHOD),
    INTERFACE(0x0200, Target.CLASS),
    ABSTRACT(0x0400, Target.CLASS, Target.METHOD),
    STRICT(0x0800, Target.METHOD),
    SYNTHETIC(0x1000, Target.CLASS, Target.FIELD, Target.METHOD, Target.PARAMETER, Target.MODULE_STAR),
    ANNOTATION(0x2000, Target.CLASS),
    ENUM(0x4000, Target.CLASS, Target.FIELD_INNER),
    MANDATED(0x8000, Target.CLASS, Target.FIELD, Target.METHOD, Target.PARAMETER, Target.MODULE_STAR),
    MODULE(0x8000, Target.CLASS),
    RECORD(0x10000, Target.CLASS),
    DEPRECATED(0x20000, Target.CLASS, Target.FIELD, Target.METHOD);

    private final int access;
    private final Target[] targets;

    Access(int access, Target... targets) {
        this.access = access;
        this.targets = targets;
    }

    public int getAccess() {
        return access;
    }

    public Target[] getTargets() {
        return targets;
    }

    public boolean isApplicable(Target target) {
        for (Target t : targets) {
            if (t == target)
                return true;
        }
        return false;
    }
}
