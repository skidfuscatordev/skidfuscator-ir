package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.arithmetic.ArithmeticOperation;
import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.exception.IllegalInstructionException;
import dev.skidfuscator.ir.primitive.Primitive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticInstructionTest {
    private ArithmeticInstruction instruction;

    @BeforeEach
    void setUp() {
        instruction = ArithmeticInstruction
                .of()
                .type(Primitive.BOOLEAN)
                .operation(ArithmeticOperation.OR)
                .build();
    }

    @AfterEach
    void tearDown() {
        instruction = null;
    }

    @Test
    void getType() {
        assertEquals(Primitive.BOOLEAN, instruction.getType());
    }

    @Test
    void setType() {
        instruction.setType(Primitive.BYTE);
        assertEquals(Primitive.BYTE, instruction.getType());
    }

    @Test
    void getOperation() {
        assertEquals(ArithmeticOperation.OR, instruction.getOperation());
    }

    @Test
    void setOperation() {
        instruction.setOperation(ArithmeticOperation.XOR);
        assertEquals(ArithmeticOperation.XOR, instruction.getOperation());
    }

    @Test
    void visitSetter() {
        instruction.copyFrom(Primitive.BYTE, ArithmeticOperation.OR);
        assertEquals(Primitive.BYTE, instruction.getType());
        assertEquals(ArithmeticOperation.OR, instruction.getOperation());
    }

    @Test
    void visitCopyToParent() {
        final ArithmeticInstruction invoke = new ArithmeticInstruction();
        instruction.copyTo(invoke);

        assertEquals(instruction.getType(), invoke.getType());
        assertEquals(instruction.getOperation(), invoke.getOperation());
    }

    @Test
    void visitCopyToChild() {
        final ArithmeticInstruction invoke = new ArithmeticInstruction();
        invoke.copyFrom(instruction);

        assertEquals(instruction.getType(), invoke.getType());
        assertEquals(instruction.getOperation(), invoke.getOperation());
    }

    @Test
    void visitCreate() {
        final InstructionList list = new InstructionList(new ArrayList<>());
        list.visitArithmetic().copyFrom(Primitive.BOOLEAN, ArithmeticOperation.OR);

        final List<Instruction> instructions = list.getInstructions();

        assertEquals(1, instructions.size());
        assertInstanceOf(ArithmeticInstruction.class, instructions.get(0));
        final ArithmeticInstruction insn = (ArithmeticInstruction) instructions.get(0);

        assertEquals(Primitive.BOOLEAN, insn.getType());
        assertEquals(ArithmeticOperation.OR, insn.getOperation());
    }

    @Test
    void of() {
        // Missing everything test
        assertThrows(IllegalInstructionException.class, () -> {
            ArithmeticInstruction.of().build();
        });

        // Missing operation test
        assertThrows(IllegalInstructionException.class, () -> {
            ArithmeticInstruction.of().type(Primitive.BYTE).build();
        });

        // Missing type test
        assertThrows(IllegalInstructionException.class, () -> {
            ArithmeticInstruction.of().operation(ArithmeticOperation.OR).build();
        });
    }

    @Test
    void illegalArrayType() {
        for (Primitive primitive : Primitive.values()) {
            // Arrays may not be used, should throw
            if (primitive.isArray()) {
                // Should throw for every case
                assertThrows(IllegalInstructionException.class, () -> {
                    ArithmeticInstruction.of()
                            .type(primitive)
                            .operation(ArithmeticOperation.OR)
                            .build();
                });
            } else {
                // Lets test all operations
                for (ArithmeticOperation value : ArithmeticOperation.values()) {
                    // If the operation is allowed, it should not throw
                    if (value.isAllowed(primitive)) {
                        assertDoesNotThrow(() -> {
                            ArithmeticInstruction.of()
                                    .type(primitive)
                                    .operation(value)
                                    .build();
                        });
                    }

                    // Illegal operation, stop here
                    else {
                        assertThrows(IllegalInstructionException.class, () -> {
                            ArithmeticInstruction.of()
                                    .type(primitive)
                                    .operation(value)
                                    .build();
                        }, "Expected a throw with ");
                    }
                }
            }
        }
    }
}