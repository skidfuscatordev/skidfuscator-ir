package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.impl.AbstractInstructionList;
import dev.skidfuscator.ir.Klass;
import dev.skidfuscator.ir.klass.internal.PrimitiveKlass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class MethodTest {

    private Klass parent;
    private MethodGroup group;
    private Method method;

    @BeforeEach
    void setUp() {
        parent = new Klass("parent");
        group = MethodGroup.of()
                .name("kek")
                .args(Collections.emptyList())
                .returnType(PrimitiveKlass.VOID)
                .methods(new ArrayList<>())
                .build();
        method = Method.of()
                .name("kek")
                .args()
                .owner(parent)
                .returnType(PrimitiveKlass.VOID)
                .group(group)
                .instructions(new AbstractInstructionList(Collections.emptyList()))
                .tags(new HashSet<>())
                .build();
    }

    @AfterEach
    void tearDown() {
        method = null;
    }

    @Test
    void getOwner() {
        assertEquals(parent, method.getOwner());
        assertEquals("parent", parent.getName());
    }

    @Test
    void setOwner() {
        Klass other = new Klass("kek2");
        method.setOwner(other);

        assertEquals(other, method.getOwner());
        assertEquals("kek2", method.getOwner().getName());

        // TODO: dual bind setting with owner class
    }

    @Test
    void getGroup() {
        assertEquals(group, method.getGroup());

        // TODO: check naming
    }

    @Test
    void setGroup() {
        // TODO: group checks
    }

    @Test
    void getName() {
        assertEquals("kek", method.getName());
    }

    @Test
    void setName() {
        assertThrowsExactly(IllegalStateException.class, () -> method.setName(null));
        assertThrowsExactly(AcknowledgeUnlinkGroupException.class, () -> method.setName("not_kek"));
        assertEquals("not_kek", method.getName());
        assertNull(method.getGroup());
        assertFalse(group.hasMethod(method));
    }

    @Test
    void getArgs() {
    }

    @Test
    void setArgs() {
    }

    @Test
    void getReturnType() {
    }

    @Test
    void setReturnType() {
    }

    @Test
    void isStatic() {
    }

    @Test
    void of() {
    }
}