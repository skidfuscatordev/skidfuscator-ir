package dev.skidfuscator.ir.hierarchy.klass;


import dev.skidfuscator.ir.Klass;

public class KlassImplementsEdge extends KlassAbstractEdge {
    public KlassImplementsEdge(Klass node, Klass inherited) {
        super(node, inherited);
    }
}
