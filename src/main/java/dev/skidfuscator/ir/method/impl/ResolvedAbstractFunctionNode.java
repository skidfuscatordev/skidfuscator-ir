package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.TryCatchBlock;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.*;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionInvoker;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ResolvedAbstractFunctionNode implements FunctionNode {
    protected final Hierarchy hierarchy;
    protected final Descriptor originalDescriptor;
    protected MethodNode node;

    protected boolean resolved;
    protected boolean mutable;

    protected KlassNode owner;
    protected InstructionList instructions;
    protected List<FunctionInvoker<?>> invokers;
    protected int access;
    private List<TryCatchBlock> tryCatchBlocks;
    protected List<KlassNode> exceptions;

    public ResolvedAbstractFunctionNode(
            final Hierarchy hierarchy,
            final Descriptor descriptor,
            final MethodNode node) {

        this.originalDescriptor = descriptor;
        this.hierarchy = hierarchy;
        this.mutable = true;

        if (node != null) {
            this.node = node;
            this.access = node.access;
        } else {
            this.access = Opcodes.ACC_PUBLIC;
        }

        this.exceptions = new ArrayList<>();
        this.invokers = new ArrayList<>();
        this.instructions = new InstructionList(this, new ArrayList<>());
        this.tryCatchBlocks = new ArrayList<>();
    }

    @Override
    public void lock() {
        this.mutable = false;
        this.instructions.lock();
    }

    @Override
    public Descriptor getOriginalDescriptor() {
        return originalDescriptor;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }

    @Override
    public void resolveHierarchy() {
        if (node == null)
            return;

        this.access = node.access;
        //Test implementation
        //TODO: Java stream api goes brrrr
        if (node.exceptions != null) {
            for (String exception : node.exceptions) {
                final KlassNode exceptionKlass = hierarchy.findClass(exception);
                this.exceptions.add(exceptionKlass);
            }
        }
    }

    @Override
    public void resolveInternal() {
        if (resolved) {
            throw new IllegalStateException(String.format(
                    "Function %s of %s is already resolved!",
                    this,
                    this.getOwner()
            ));
        }
        if (node == null) {
            resolved = true;
            return;
        }

        for (AbstractInsnNode instruction : this.node.instructions) {
            final Insn<?> insn;
            if (instruction instanceof MethodInsnNode) {
                insn = new InvokeInsn(
                        hierarchy,
                        (MethodInsnNode) instruction
                );
            }

            else if (instruction instanceof FieldInsnNode) {
                insn = new FieldInsn(
                        hierarchy,
                        (FieldInsnNode) instruction
                );
            }

            else if (instruction instanceof MultiANewArrayInsnNode) {
                insn = new MultiANewArrayInsn(
                        hierarchy,
                        (MultiANewArrayInsnNode) instruction
                );
            }

            else if (instruction instanceof TypeInsnNode) {
                insn = new TypeInsn(
                        hierarchy,
                        (TypeInsnNode) instruction
                );
            }

            else if (instruction instanceof LdcInsnNode) {
                insn = new LdcInsn(
                        hierarchy,
                        (LdcInsnNode) instruction
                );
            }

            else if (instruction instanceof InvokeDynamicInsnNode) {
                insn = new InvokeDynamicInsn(
                        hierarchy,
                        (InvokeDynamicInsnNode) instruction
                );
            }

            else if (instruction instanceof IntInsnNode) {
                insn = new IntInsn(
                        hierarchy,
                        (IntInsnNode) instruction
                );
            }

            else if (instruction instanceof InsnNode) {
                insn = new SimpleInsn(
                        hierarchy,
                        (InsnNode) instruction
                );
            }

            else if (instruction instanceof IincInsnNode) {
                insn = new IincInsn(
                        hierarchy,
                        (IincInsnNode) instruction
                );
            }

            else if (instruction instanceof JumpInsnNode) {
                insn = new JumpInsn(
                        hierarchy,
                        (JumpInsnNode) instruction
                );
            }

            else if (instruction instanceof LookupSwitchInsnNode) {
                insn = new LookupSwitchInsn(
                        hierarchy,
                        (LookupSwitchInsnNode) instruction
                );
            }

            else if (instruction instanceof TableSwitchInsnNode) {
                insn = new TableSwitchInsn(
                        hierarchy,
                        (TableSwitchInsnNode) instruction
                );
            }

            else if (instruction instanceof VarInsnNode) {
                insn = new VarInsn(
                        hierarchy,
                        (VarInsnNode) instruction
                );
            }

            else if (instruction instanceof LabelNode) {
                insn = new LabelInsn(
                        hierarchy,
                        (LabelNode) instruction
                );
            }

            else if (instruction instanceof FrameNode) {
                insn = new FrameInsn(
                        hierarchy,
                        (FrameNode) instruction
                );
            }

            else
                throw new IllegalStateException("Unknown instruction " + instruction.getClass().getSimpleName());


            this.instructions.add(insn);
        }

        for (Insn<?> instruction : instructions) {
            instruction.resolve();
        }

        if (!mutable) {
            this.instructions.lock();
        }

        for (TryCatchBlockNode tryCatchBlock : this.node.tryCatchBlocks) {
            tryCatchBlocks.add(new TryCatchBlock(
                    hierarchy,
                    instructions,
                    tryCatchBlock
            ));
        }

        for (TryCatchBlock tryCatchBlock : tryCatchBlocks) {
            tryCatchBlock.resolve();
        }

        this.resolved = true;
    }

    @Override
    public MethodNode dump() {
        if (this.node == null) {
            if (instructions.isEmpty()) {
                return null;
            }

            this.node = new MethodNode();
        }

        this.node.name = this.getName();
        this.node.desc = this.getDesc();
        this.node.access = this.access;

        this.node.instructions = new InsnList();
        for (Insn<?> instruction : this.instructions) {
            this.node.instructions.add(instruction.dump());
        }

        this.node.tryCatchBlocks = new ArrayList<>();
        for (TryCatchBlock tryCatchBlock : this.tryCatchBlocks) {
            this.node.tryCatchBlocks.add(tryCatchBlock.dump());
        }

        return node;
    }

    @Override
    public List<FunctionInvoker<?>> getInvokes() {
        return Collections.unmodifiableList(invokers);
    }

    @Override
    public void addInvoke(FunctionInvoker<?> invoker) {
        this.invokers.add(invoker);

        assert invoker.getTarget() == this : "The target invocation needs to set to this function before adding!";
    }

    @Override
    public void removeInvoke(FunctionInvoker<?> invoker) {
        this.invokers.remove(invoker);

        assert invoker.getTarget() == null : "The target invocation needs to set to null before removing!";
    }

    @Override
    public InstructionList getInstructions() {
        _checkResolve();
        return instructions;
    }

    @Override
    public List<TryCatchBlock> getTryCatchBlocks() {
        _checkResolve();
        return Collections.unmodifiableList(tryCatchBlocks);
    }

    @Override
    public List<KlassNode> getExceptions() {
        _checkResolve();
        return exceptions;
    }

    @Override
    public KlassNode getOwner() {
        return owner;
    }

    @Override
    public void setOwner(KlassNode node) {
        if (!mutable)
            throw new IllegalStateException("Cannot set owner of a non-mutable method descriptor!");

        // Properly de-assign
        if (this.owner != null) {
            this.owner.removeMethod(this);
            this.owner = null;
        }

        this.owner = node;
        node.addMethod(this);
    }

    @Override
    public String getName() {
        return this.originalDescriptor.getName();
    }

    @Override
    public void setName(final String name) {
        throw new IllegalStateException("Cannot set name of a non-mutable method descriptor!");
    }

    @Override
    public String getDesc() {
        return this.originalDescriptor.getDesc();
    }

    @Override
    public boolean isStatic() {
        return (this.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public boolean isConstructor() {
        return !resolved ? this.getOriginalDescriptor().getName().equals("<init>") : this.getName().equals("<init>");
    }

    private void _checkResolve() {
        assert resolved : String.format(
                "Function %s#%s%s needs to be resolved!",
                owner.getName(),
                node.name,
                node.desc
        );
    }

    @Override
    public String toString() {
        return getOwner().getName() + "." + getName() + getDesc();
    }
}
