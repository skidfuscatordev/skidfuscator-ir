package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.insn.InstructionsVisitor;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IincInstructionTest {

    @Test
    void testBuilder() {
        final int local = 1;
        final int increment = 5;

        final IincInstruction instruction = IincInstruction.of()
                .local(local)
                .increase(increment)
                .build();

        assertNotNull(instruction);
        assertEquals(local, instruction.getLocal());
        assertEquals(increment, instruction.getIncrease());
    }

    @Test
    void testCopyTo() {
        final int local = 1;
        final int increment = 5;

        InstructionsVisitor mockVisitor = mock(InstructionsVisitor.class);
        IincInstruction instruction = IincInstruction.of()
                .local(local)
                .increase(increment)
                .build();

        instruction.copyTo(mockVisitor);

        verify(mockVisitor).visitIinc(local, increment);
    }

    @Test
    void testSetAndGetLocal() {
        final int initialLocal = 1;
        final int newLocal = 2;

        IincInstruction instruction = new IincInstruction();
        instruction.setLocal(initialLocal);
        assertEquals(initialLocal, instruction.getLocal());

        instruction.setLocal(newLocal);
        assertEquals(newLocal, instruction.getLocal());
    }

    @Test
    void testSetAndGetIncrease() {
        final int initialIncrease = 5;
        final int newIncrease = 10;

        IincInstruction instruction = new IincInstruction();
        instruction.setIncrease(initialIncrease);
        assertEquals(initialIncrease, instruction.getIncrease());

        instruction.setIncrease(newIncrease);
        assertEquals(newIncrease, instruction.getIncrease());
    }
}
