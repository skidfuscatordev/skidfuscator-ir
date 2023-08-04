package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.method.FunctionInheritanceEdge;
import dev.skidfuscator.ir.insn.InstructionList;
import dev.skidfuscator.ir.insn.TryCatchBlock;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.*;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionInvoker;
import dev.skidfuscator.ir.type.TypeWrapper;
import dev.skidfuscator.ir.util.Descriptor;
import dev.skidfuscator.ir.variable.LocalVariable;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.stream.Collectors;

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
    protected List<LocalVariable> localVariables;

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
        this.localVariables = node != null && node.localVariables != null
                ? node.localVariables.stream().map(localVariableNode -> new LocalVariable(localVariableNode, hierarchy)).collect(Collectors.toList())
                : List.of();
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
    public Set<FunctionNode> getParents() {
        return Collections.unmodifiableSet(hierarchy.getFunctionGraph().parentsOf(this));
    }

    @Override
    public void addParent(FunctionNode functionNode) {
        if (functionNode == this) {
            throw new IllegalStateException(String.format(
                    "Cannot add a null function as a parent of %s",
                    this.toString()
            ));
        }

        if (this.hasParent(functionNode)) {
            throw new IllegalStateException(String.format(
                    "Cannot add %s as a parent of %s because it is already a parent",
                    functionNode.toString(),
                    this.toString()
            ));
        }

        hierarchy.getFunctionGraph().addEdge(
                this,
                functionNode,
                new FunctionInheritanceEdge(functionNode, this)
        );
    }

    @Override
    public void removeParent(FunctionNode functionNode) {
        hierarchy.getFunctionGraph().removeEdge(
                hierarchy.getFunctionGraph().getEdge(functionNode, this)
        );
    }

    @Override
    public boolean hasParent(FunctionNode functionNode) {
        return this.getParents().contains(functionNode);
    }

    @Override
    public Set<FunctionNode> getChildren() {
        return Collections.unmodifiableSet(
                hierarchy.getFunctionGraph().childrenOf(this)
        );
    }

    @Override
    public void addChild(FunctionNode functionNode) {
        if (functionNode == this) {
            throw new IllegalStateException(String.format(
                    "Cannot add a null function as a child of %s",
                    this.toString()
            ));
        }

        if (this.hasChild(functionNode)) {
            throw new IllegalStateException(String.format(
                    "Cannot add a child that already exists for %s",
                    this.toString()
            ));
        }

        this.hierarchy.getFunctionGraph().addEdge(
                functionNode,
                this,
                new FunctionInheritanceEdge(this, functionNode)
        );
    }

    @Override
    public void removeChild(FunctionNode functionNode) {
        this.hierarchy.getFunctionGraph().removeEdge(
                hierarchy.getFunctionGraph().getEdge(functionNode, this)
        );
    }

    @Override
    public boolean hasChild(FunctionNode functionNode) {
        return this.getChildren().contains(functionNode);
    }

    @Override
    public void resolveHierarchy() {
        if (node == null)
            return;

        for (LocalVariable localVariable : localVariables) {
            localVariable.resolveHierarchy();
        }

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
            //System.err.println("WARN! Function " + this + " is synthetic!");
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

            else if (instruction instanceof LineNumberNode) {
                insn = new LineNumberInsn(
                        hierarchy,
                        (LineNumberNode) instruction
                );
            }

            else
                throw new IllegalStateException("Unknown instruction " + instruction.getClass().getSimpleName());


            this.instructions.add(insn);
        }

        for (Insn<?> instruction : instructions) {
            try {
                instruction.resolve();
            } catch (IllegalStateException e) {
                throw new IllegalStateException(String.format(
                        "Failed to resolve instruction %s of %s",
                        instruction.getClass().getName(),
                        this
                ), e);
            }
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

        if (owner.getName().contains("ha") && node.name.equals("<clinit>")) {
            System.out.println(String.format(
                    "Resolved %s of %s with instructions: \n%s",
                    this,
                    this.getOwner(),
                    this.instructions.print()
            ));
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
        this.node.signature = this.getSignature();
        this.node.access = this.access;

        this.node.instructions = new InsnList();
        for (Insn<?> instruction : this.instructions) {
            this.node.instructions.add(instruction.dump());
        }

        this.node.tryCatchBlocks = new ArrayList<>();
        for (TryCatchBlock tryCatchBlock : this.tryCatchBlocks) {
            this.node.tryCatchBlocks.add(tryCatchBlock.dump());
        }

        this.node.localVariables = new ArrayList<>();
        for (LocalVariable localVariable : localVariables) {
            this.node.localVariables.add(localVariable.dump());
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
        return instructions;
    }

    @Override
    public List<TryCatchBlock> getTryCatchBlocks() {
        _checkResolve();
        return Collections.unmodifiableList(tryCatchBlocks);
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        _checkResolve();
        return Collections.unmodifiableList(localVariables);
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

        if (node == null) {
            throw new IllegalArgumentException(String.format(
                    "Cannot set owner of %s to null!",
                    this.getOriginalDescriptor()
            ));
        }

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

    public String getSignature() {
        return this.node == null ? "" : this.node.signature;
    }

    @Override
    public boolean isStatic() {
        return (this.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public boolean isPrivate() {
        return (this.access & Opcodes.ACC_PRIVATE) != 0;
    }

    @Override
    public boolean isConstructor() {
        return !resolved ? this.getOriginalDescriptor().getName().equals("<init>") : this.getName().equals("<init>");
    }

    @Override
    public boolean isClassInit() {
        return !resolved ? this.getOriginalDescriptor().getName().equals("<clinit>") : this.getName().equals("<clinit>");
    }

    protected void _checkResolve() {
        assert resolved : String.format(
                "Function %s#%s%s needs to be resolved!",
                owner.getName(),
                originalDescriptor.getName(),
                originalDescriptor.getDesc()
        );
    }

    @Override
    public String toString() {
        return getOwner().getName() + "." + getName() + getDesc();
    }
}
