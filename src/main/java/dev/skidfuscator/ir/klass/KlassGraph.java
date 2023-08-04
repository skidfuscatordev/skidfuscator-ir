package dev.skidfuscator.ir.klass;

import dev.skidfuscator.ir.hierarchy.klass.KlassInheritanceEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.*;
import java.util.stream.Collectors;

public class KlassGraph extends DirectedAcyclicGraph<KlassNode, KlassInheritanceEdge> {
    public KlassGraph() {
        super(KlassInheritanceEdge.class);
    }

    public Set<KlassNode> childrenOf(KlassNode node) {
        return incomingEdgesOf(node)
                .stream()
                .map(KlassInheritanceEdge::getNode)
                .collect(Collectors.toSet());
    }

    public Set<KlassNode> parentsOf(KlassNode node) {
        return outgoingEdgesOf(node)
                .stream()
                .map(KlassInheritanceEdge::getInherited)
                .collect(Collectors.toSet());
    }

    public Collection<KlassNode> getCommonAncestor(Collection<KlassNode> nodes) {
        /*
         * Really shite O(3h) algorithm because I'm too lazy to actually learn one
         *
         * h: maximum height of graph
         *
         *
         */
        final Map<KlassNode, Integer> depthMap = new HashMap<>();
        final Map<Integer, Map<KlassNode, List<KlassNode>>> ancestorMap = new HashMap<>();

        int lowestDepth = 9999;

        /*
         * Here we first compute the max depth of all the branches
         * since we know they all have the common ancestor of
         * java/lang/Object so yeah...
         *
         * Btw we have to get the depth before to be able to invert
         * it
         */
        for (KlassNode node : nodes) {
            int maxDepth = 0;

            List<KlassNode> above = new ArrayList<>();
            /* Add the root node */
            above.add(node);

            while (true) {
                /* Create a temp stack */
                final List<KlassNode> stack = new ArrayList<>(above);

                /* Clear the heading */
                above.clear();

				/* Add all the successors to all the
				   previous layers.
				 */
                for (KlassNode n : stack) {
                    above.addAll(this.parentsOf(n));
                }

                if (above.isEmpty())
                    break;

                maxDepth++;
            }

            depthMap.put(node, maxDepth);

            if (lowestDepth > maxDepth) {
                lowestDepth = maxDepth;
            }
        }

        /*
         * Here we directly compute all the hierarchy they have
         * at a specified branch height which is inverted
         */
        for (KlassNode node : nodes) {
            /* Get the max depth to properly invert it */
            int maxDepth = depthMap.get(node);

            List<KlassNode> above = new ArrayList<>();
            above.add(node);

            while (maxDepth >= 0) {
                final List<KlassNode> stack = new ArrayList<>(above);
                above.clear();
                for (KlassNode n : stack) {
                    above.addAll(this.parentsOf(n));
                }

                /* Slight optimization since we're not gonna bother with anything higher */
                if (maxDepth <= lowestDepth) {
                    ancestorMap
                            .computeIfAbsent(maxDepth, e -> new HashMap<>())
                            .put(node, stack);
                }

                maxDepth--;
            }
        }

        int depth = lowestDepth;
        final List<KlassNode> common = new ArrayList<>();

        while (depth >= 0) {
            final Map<KlassNode, List<KlassNode>> checked = ancestorMap.get(depth);

            final Set<KlassNode> visited = new HashSet<>();

            /*
             * If there's any common match at a specific height, then
             * by default it is the lowest common node but since it's
             * a graph, it can be multiple:
             *
             *      O
             *     / \
             *    KlassNode1 KlassNode2
             *     \ /
             *     / \
             *    KlassNode4 KlassNode5
             *
             * Both KlassNode4 and KlassNode5 have KlassNode1 and KlassNode2 as common ancestors
             */
            for (List<KlassNode> value : checked.values()) {
                for (KlassNode n : value) {
                    if (visited.contains(n))
                        continue;

                    if (checked.values().stream().allMatch(e -> e.contains(n))) {
                        common.add(n);
                    }

                    visited.add(n);
                }

            }

            /* We have a common ancestor, since we want the lowest one we grab this */
            if (!common.isEmpty())
                break;

            depth--;
        }
        return common;
    }
}
