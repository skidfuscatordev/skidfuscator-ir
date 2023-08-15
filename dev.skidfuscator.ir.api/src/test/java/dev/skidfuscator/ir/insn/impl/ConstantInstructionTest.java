package dev.skidfuscator.ir.insn.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantInstructionTest {
    private ConstantInstruction instruction;

    @BeforeEach
    void setUp() {
        this.instruction = ConstantInstruction
                .of()
                .constant("test")
                .build();
    }

    @AfterEach
    void tearDown() {
        this.instruction = null;
    }

    @Test
    void getConstant() {
        assertEquals("test", instruction.getConstant());
    }

    @Test
    void setConstant() {
        instruction.setConstant("no");
        assertEquals("no", instruction.getConstant());
    }

    @Test
    void of() {
        assertDoesNotThrow(() -> {
            ConstantInstruction.of().build();
        });
    }
}