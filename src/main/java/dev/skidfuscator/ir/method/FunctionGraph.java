package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.method.FunctionInheritanceEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class FunctionGraph extends DefaultUndirectedGraph<FunctionNode, FunctionInheritanceEdge> {
    public FunctionGraph() {
        super(FunctionInheritanceEdge.class);
    }
}
