package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.field.FieldInheritanceEdge;
import dev.skidfuscator.ir.hierarchy.method.FunctionInheritanceEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class FieldGraph extends DefaultUndirectedGraph<FieldNode, FieldInheritanceEdge> {
    public FieldGraph() {
        super(FieldInheritanceEdge.class);
    }
}
