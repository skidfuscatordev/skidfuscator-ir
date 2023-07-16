package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.hierarchy.klass.KlassInheritanceEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class KlassGraph extends SimpleDirectedGraph<KlassNode, KlassInheritanceEdge> {
    public KlassGraph() {
        super(KlassInheritanceEdge.class);
    }
}
