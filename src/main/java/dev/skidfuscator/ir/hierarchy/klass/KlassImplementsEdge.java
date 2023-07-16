package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.klass.KlassNode;

public class KlassImplementsEdge extends KlassAbstractEdge {
    public KlassImplementsEdge(KlassNode node, KlassNode inherited) {
        super(node, inherited);
    }
}
