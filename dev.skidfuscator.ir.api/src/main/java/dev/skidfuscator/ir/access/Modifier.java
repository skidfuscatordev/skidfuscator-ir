package dev.skidfuscator.ir.access;

import dev.skidfuscator.ir.access.exception.IllegalAccessException;
import dev.skidfuscator.ir.verify.Assert;

import java.util.HashSet;
import java.util.Set;

public abstract class Modifier {
    private final Target target;
    private int access;

    public Modifier(Target target, int access) {
        this.target = target;
        this.access = access;
    }

    public static void remove(Modifier modifier, Access access) {
        if (is(modifier, access))
            modifier.setAccess(modifier.getAccess() & ~access.getAccess());
    }

    public static void add(Modifier modifier, Access access) {
        Assert.isTrue(access.isApplicable(modifier.getTarget()), new IllegalAccessException("%s access does not belong to %s".formatted(access, modifier.getTarget())));
        if (!is(modifier, access))
            modifier.setAccess(modifier.getAccess() | access.getAccess());
    }

    public static boolean is(Modifier modifier, Access access) {
        return (modifier.getAccess() & access.getAccess()) != 0;
    }

    public void set(Access... accesses) {
        for (Access access : accesses) {
            add(this, access);
        }
    }

    public void remove(Access... accesses) {
        for (Access access : accesses) {
            remove(this, access);
        }
    }

    public void setPublic() {
        remove(Access.PRIVATE, Access.PROTECTED);
        set(Access.PUBLIC);
    }

    public void setPrivate() {
        remove(Access.PUBLIC, Access.PROTECTED);
        set(Access.PRIVATE);
    }

    public void setProtected() {
        remove(Access.PRIVATE, Access.PUBLIC);
        set(Access.PROTECTED);
    }

    public void clearAccess() {
        remove(Access.PUBLIC, Access.PRIVATE, Access.PROTECTED);
    }

    public void resetAccess() {
        this.access = 0;
    }

    //Why not?
    public Set<Access> toSet() {
        Set<Access> accesses = new HashSet<>();
        for (Access value : Access.values()) {
            if (!value.isApplicable(target))
                continue;

            accesses.add(value);
        }

        return accesses;
    }

    public boolean is(Access access) {
        return is(this, access);
    }

    public int getAccess() {
        return access;
    }

    private void setAccess(int access) {
        this.access = access;
    }

    public Target getTarget() {
        return target;
    }
}
