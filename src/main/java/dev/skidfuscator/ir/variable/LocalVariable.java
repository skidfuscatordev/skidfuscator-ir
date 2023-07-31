package dev.skidfuscator.ir.variable;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.signature.SignatureWrapper;
import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LocalVariableNode;

public class LocalVariable implements HierarchyResolvable {

    private final LocalVariableNode node;

    private TypeWrapper desc;
    private SignatureWrapper sig;

    public LocalVariable(LocalVariableNode node, Hierarchy hierarchy) {
        this.node = node;
        this.desc = new TypeWrapper(Type.getType(node.desc), hierarchy);
        this.sig = new SignatureWrapper(node.signature, hierarchy);
    }

    @Override
    public void resolveHierarchy() {
        if (!desc.isResolved()) {
            sig.resolveHierarchy();
        }

        if (!sig.isResolved()) {
            sig.resolveHierarchy();
        }
    }

    public LocalVariableNode dump() {
        if (desc.isResolved()) {
            node.desc = desc.dump().getDescriptor();
        }

        if (sig.isResolved()) {
            node.signature = sig.dump();
        }

        return node;
    }
}
