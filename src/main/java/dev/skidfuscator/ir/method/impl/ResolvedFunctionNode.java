package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.Insn;
import dev.skidfuscator.ir.insn.impl.*;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionGroup;
import dev.skidfuscator.ir.method.FunctionInvoker;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class ResolvedFunctionNode implements FunctionNode {
    private final MethodNode node;
    private final Hierarchy hierarchy;

    private KlassNode parent;
    private FunctionGroup group;

    private List<Insn> instructions;
    private List<FunctionInvoker<?>> invokers;
    private int access;

    private List<KlassNode> exceptions;

    public ResolvedFunctionNode(MethodNode node, Hierarchy hierarchy) {
        this.node = node;
        this.hierarchy = hierarchy;
        this.access = node.access;
        this.instructions = new ArrayList<>();
        this.invokers = new ArrayList<>();
    }

    @Override
    public void dump() {
        this.node.name = this.getName();
        this.node.desc = this.getDesc();
        this.node.access = this.access;

        this.node.instructions = new InsnList();
        for (Insn instruction : this.instructions) {
            this.node.instructions.add(instruction.dump());
        }
    }

    @Override
    public FunctionGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(FunctionGroup group) {
        if (this.group != null && group != null) {
            throw new IllegalStateException(String.format(
                    "Conflict! Groups intersected: %s v %s",
                    this.getName() + this.getDesc(),
                    group.getName() + group.getDesc()
            ));
        }
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
    public void resolve() {
        this.access = node.access;
        this.instructions = new ArrayList<>();

        //Test implementation
        //TODO: Java stream api goes brrrr
        if (node.exceptions != null) {
            this.exceptions = new ArrayList<>();
            for (String exception : node.exceptions) {
                this.exceptions.add(hierarchy.findClass(exception));
            }
        }

        for (AbstractInsnNode instruction : this.node.instructions) {
            final Insn insn;
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

            else
                continue;


            this.instructions.add(insn);
        }

        FunctionGroup group = null;

        /*
         * Only non-static methods need to resolve
         * groups. Static methods may simply just
         * create their groups without worrying
         * about conflicts.
         */
        if (!isStatic() && !node.name.equals("<init>")) {
            final Stack<KlassNode> stack = new Stack<>();
            stack.add(parent.getParent());
            stack.addAll(parent.getInterfaces());

            while (!stack.isEmpty()) {
                final KlassNode klass = stack.pop();

                // Skip java.lang.Object
                if (klass == null)
                    continue;

                final FunctionNode similar = hierarchy.findMethod(
                        klass.getName(),
                        this.node.name,
                        this.node.desc
                );

                if (similar == null) {
                    stack.add(klass.getParent());
                    stack.addAll(klass.getInterfaces());
                    continue;
                }

                if (similar.getGroup() == null) {
                    throw new IllegalStateException(String.format(
                            "Parent of klass %s has similar method but method group is unresolved! " +
                                    "This means that the class path has not been resolved from tree root. Failing."
                            , klass.getName()));
                }

                if (group != null && !similar.getGroup().equals(group)) {
                    throw new IllegalStateException(String.format(
                            "Parent of klass %s has similar method but method group is conflictual! " +
                                    "This means that the class path has not been resolved from tree root. Failing."
                            , klass.getName()));
                }

                group = similar.getGroup();
            }
        }


        this.group = group == null
                ? new FunctionGroup(this.node.name, this.node.desc)
                : group;
    }

    @Override
    public List<Insn> getInstructions() {
        return instructions;
    }

    @Override
    public List<KlassNode> getExceptions() {
        return exceptions;
    }

    @Override
    public KlassNode getParent() {
        return parent;
    }

    @Override
    public void setParent(KlassNode node) {
        this.parent = node;
    }

    @Override
    public String getName() {
        return this.group.getName();
    }

    @Override
    public void setName(final String name) {
        this.group.setName(name);
    }

    @Override
    public String getDesc() {
        return this.group.getDesc();
    }

    @Override
    public boolean isStatic() {
        return (this.access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public boolean isConstructor() {
        return this.getName().equals("<init>");
    }

    @Override
    public String toString() {
        return getParent().getName() + "." + getName() + getDesc();
    }
}
