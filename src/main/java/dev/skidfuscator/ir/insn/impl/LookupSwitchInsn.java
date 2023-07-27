package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class LookupSwitchInsn extends AbstractInsn<LookupSwitchInsnNode> {
    private Map<Integer, LabelInsn> keyMap;
    private LabelInsn dflt;
    public LookupSwitchInsn(Hierarchy hierarchy, LookupSwitchInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public void resolve() {
        this.keyMap = new HashMap<>();

        for (int i = 0; i < this.node.labels.size(); i++) {
            final int key = this.node.keys.get(i);
            final LabelNode label = this.node.labels.get(i);

            keyMap.put(key, this.getParent().getLabel(label));
        }
    }

    @Override
    public LookupSwitchInsnNode dump() {
        this.node.labels.clear();
        this.node.keys.clear();
        this.keyMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(e -> {
                    this.node.keys.add(e.getKey());
                    this.node.labels.add(e.getValue().dump());
                });
        this.node.dflt = this.dflt.dump();

        return node;
    }


    public Map<Integer, LabelInsn> getKeyMap() {
        return keyMap;
    }

    public LabelInsn getDflt() {
        return dflt;
    }
}
