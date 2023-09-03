package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.arithmetic.ArithmeticOperation;
import dev.skidfuscator.ir.insn.Instruction;
import dev.skidfuscator.ir.insn.InstructionsVisitor;
import dev.skidfuscator.ir.insn.exception.IllegalInstructionException;
import dev.skidfuscator.ir.insn.impl.visitor.ArithmeticInstructionVisitor;
import dev.skidfuscator.ir.primitive.Primitive;
import dev.skidfuscator.ir.verify.Assert;
import org.jetbrains.annotations.NotNull;

public class ArithmeticInstruction extends ArithmeticInstructionVisitor implements Instruction {
    private Primitive type;
    private ArithmeticOperation operation;

    protected ArithmeticInstruction() {
        super();
    }

    public ArithmeticInstruction(ArithmeticInstructionVisitor parent) {
        super(parent);
    }

    public static Builder of() {
        return new Builder();
    }

    public @NotNull Primitive getType() {
        assertNotNull(type);
        return type;
    }

    public void setType(Primitive type) {
        Assert.nonNull(type, new IllegalInstructionException(
                "Arithmetic instruction cannot have null type"
        ));
        this.type = type;
    }

    public @NotNull ArithmeticOperation getOperation() {
        assertNotNull(type);
        return operation;
    }

    public void setOperation(ArithmeticOperation operation) {
        Assert.nonNull(operation, new IllegalInstructionException(
                "Arithmetic instruction cannot have null operation"
        ));
        Assert.isTrue(operation.isAllowed(type), new IllegalInstructionException(
                "Arithmetic operand %s does not allow %s", operation, type)
        );

        this.operation = operation;
    }

    /**
     * Basic visitor pattern renamed to help with beginners
     * without a CS degree. This is synonymous to visit.
     *
     * @param visitor Visitor being visited
     */
    @Override
    public void copyTo(InstructionsVisitor visitor) {
        copyTo(visitor.visitArithmetic());
    }

    public void copyTo(ArithmeticInstructionVisitor visitor) {
        visitor.copyFrom(type, operation);
    }

    @Override
    public void copyFrom(Primitive type, ArithmeticOperation operation) {
        this.setType(type);
        this.setOperation(operation);

        super.copyFrom(type, operation);
    }

    public void copyFrom(ArithmeticInstruction visitor) {
        visitor.copyTo(this);
    }

    public static final class Builder {
        private Primitive type;
        private ArithmeticOperation operation;

        private Builder() {
        }

        public Builder type(Primitive type) {
            this.type = type;
            return this;
        }

        public Builder operation(ArithmeticOperation operation) {
            this.operation = operation;
            return this;
        }

        public ArithmeticInstruction build() {
            final ArithmeticInstruction instruction = new ArithmeticInstruction();
            instruction.copyFrom(type, operation);

            return instruction;
        }
    }
}
