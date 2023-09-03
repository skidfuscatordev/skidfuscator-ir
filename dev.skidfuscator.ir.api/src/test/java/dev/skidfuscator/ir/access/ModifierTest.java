package dev.skidfuscator.ir.access;

import dev.skidfuscator.ir.Field;
import dev.skidfuscator.ir.access.exception.IllegalAccessException;
import dev.skidfuscator.ir.access.impl.FieldModifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModifierTest {

    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
        field.setModifier(FieldModifier.of()._public()._static().build());
    }

    @Test
    void isAccessTest() {
        assertTrue(field.isStatic());
        assertFalse(field.isFinal());
    }

    @Test
    void illegalAccessTarget() {
        assertThrows(IllegalAccessException.class, () -> {
            field.getModifier().set(Access.BRIDGE);
        });
    }

    @Test
    void removeAccessTest() {
        field.getModifier().remove(Access.STATIC);
        assertFalse(field.isStatic());
    }

    @Test
    void removeNothingTest() {
        int temp = field.getModifier().getAccess();
        field.getModifier().remove(Access.FINAL);
        assertEquals(temp, field.getModifier().getAccess());
    }

    @Test
    void addAccessTest() {
        field.getModifier().set(Access.VOLATILE);
        assertTrue(field.isVolatile());
    }

    @Test
    void accessTest() {
        field.setPublic();
        assertTrue(field.isPublic());

        field.setProtected();
        assertFalse(field.isPublic());
        assertTrue(field.isProtected());

        field.setStatic();
        assertTrue(field.isStatic());

        field.setNotStatic();
        assertFalse(field.isStatic());

        field.getModifier().set(Access.VOLATILE);
        field.clearAccess();
        assertFalse(field.isPublic());
        assertFalse(field.isPrivate());
        assertFalse(field.isProtected());
        assertTrue(field.isVolatile());

        field.resetAccess();
        assertFalse(field.isVolatile());

        assertEquals(0, field.getModifier().getAccess());
    }

    @Test
    void multipleSetTest() {
        field.getModifier().set(Access.VOLATILE);
        field.getModifier().set(Access.TRANSIENT);

        int tempAccess = field.getModifier().getAccess();
        field.getModifier().set(Access.VOLATILE);
        field.getModifier().set(Access.TRANSIENT);

        assertEquals(tempAccess, field.getModifier().getAccess());
    }

    @AfterEach
    void tearDown() {
        field = null;
    }
}
