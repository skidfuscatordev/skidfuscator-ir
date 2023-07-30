package dev.skidfuscator.ir.method;

import dev.skidfuscator.ir.hierarchy.method.FunctionInheritanceEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.Set;
import java.util.stream.Collectors;

public class FunctionGraph extends DirectedAcyclicGraph<FunctionNode, FunctionInheritanceEdge> {
    public FunctionGraph() {
        super(FunctionInheritanceEdge.class);
    }

    public Set<FunctionNode> childrenOf(FunctionNode node) {
        return incomingEdgesOf(node)
                .stream()
                .map(FunctionInheritanceEdge::getChild)
                .collect(Collectors.toSet());
    }

    public Set<FunctionNode> parentsOf(FunctionNode node) {
        return outgoingEdgesOf(node)
                .stream()
                .map(FunctionInheritanceEdge::getParent)
                .collect(Collectors.toSet());
    }
}
