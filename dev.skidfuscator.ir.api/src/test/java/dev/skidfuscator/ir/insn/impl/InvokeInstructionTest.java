package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.Method;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.impl.visitor.InvokeInstructionVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class InvokeInstructionTest {
    private InvokeInstruction instruction;
    private Method mockMethod;

    @BeforeEach
    void setUp() {
        // Assuming a simple mock using Mockito
        mockMethod = mock(Method.class);
        when(mockMethod.isStatic()).thenReturn(true); // Assume the method is static for initial setup

        this.instruction = InvokeInstruction
                .of()
                .target(mockMethod)
                .build();
    }

    @AfterEach
    void tearDown() {
        this.instruction = null;
    }

    @Test
    void getTarget() {
        assertEquals(mockMethod, instruction.getTarget(), "The target method should be returned correctly.");
    }

    @Test
    void setTarget() {
        Method newMethod = mock(Method.class); // Create another mock method
        instruction.setTarget(newMethod);
        assertEquals(newMethod, instruction.getTarget(), "The target method should be updated correctly.");
    }

    @Test
    void testIsStatic() {
        assertTrue(instruction.isStatic(), "Should return true for a static method.");
        // Change method static status for further verification
        when(mockMethod.isStatic()).thenReturn(false);
        instruction.setTarget(mockMethod); // Update target to reflect change
        assertFalse(instruction.isStatic(), "Should return false for a non-static method.");
    }

    @Test
    void of() {
        assertDoesNotThrow(() -> {
            InvokeInstruction.of().target(mockMethod).build();
        }, "Building an InvokeInstruction with a target should not throw an exception.");
    }

    @Test
    void checkNullTargetHandling() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            InvokeInstruction.of().target(null).build();
        }, "Should throw IllegalArgumentException when trying to set null as a target.");

        assertTrue(exception.getMessage().contains("Cannot set invocation target to null"), "Exception message should indicate the issue with null");
    }

    @Test
    void testSetNullTargetDirectly() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> instruction.setTarget(null),
                "Directly setting null as target should throw IllegalArgumentException.");
        assertTrue(exception.getMessage().contains("Cannot set invocation target to null"), "Correct exception message should be displayed.");
    }

    @Test
    void testIllegalStateWithBuilder() {
        // Attempting to build an instruction without setting a target
        assertThrows(IllegalArgumentException.class, () -> InvokeInstruction.of().build(),
                "Building without setting a target should throw IllegalStateException.");
    }

    @Test
    void testVisitorInteractionWithIllegalState() {
        InstructionsVisitor visitor = mock(InstructionsVisitor.class);
        InvokeInstructionVisitor invokeVisitor = mock(InvokeInstructionVisitor.class);

        when(visitor.visitInvoke()).thenReturn(invokeVisitor);

        // Assume visitor's interaction leads to an illegal state (e.g., null target set)
        doAnswer(invocation -> {
            Method method = invocation.getArgument(0);
            if (method == null) {
                throw new IllegalArgumentException("Visitor interaction cannot accept null method");
            }
            return null;
        }).when(invokeVisitor).copyFrom(any(Method.class));

        // Test the response to the illegal state
        Exception exception = assertThrows(IllegalArgumentException.class, () -> instruction.copyTo(visitor),
                "Illegal visitor interaction should raise an exception.");
        assertTrue(exception.getMessage().contains("Visitor interaction cannot accept null method"), "Correct exception message should be displayed.");
    }

    @Test
    void testStaticStatusChangeHandling() {
        // Assume the target method changes its static status unexpectedly
        when(mockMethod.isStatic()).thenReturn(false);  // Mock change to non-static
        assertFalse(instruction.isStatic(), "isStatic should reflect the updated static status.");
    }
}
