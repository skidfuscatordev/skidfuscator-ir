package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.FunctionNode;
import dev.skidfuscator.ir.hierarchy.method.FunctionInheritanceEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class MethodGraph extends DefaultUndirectedGraph<FunctionNode, FunctionInheritanceEdge> {
    public MethodGraph() {
        super(FunctionInheritanceEdge.class);
    }
}
