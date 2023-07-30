package dev.skidfuscator.ir.signature;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.klass.KlassNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SignatureWrapper implements HierarchyResolvable {

    private final Hierarchy hierarchy;

    private final String signature;
    private String resolvedSignature = "";
    private boolean resolved = false;
    private final List<KlassNode> classes = new LinkedList<>();

    public SignatureWrapper(String signature, Hierarchy hierarchy) {
        this.signature = signature;
        this.hierarchy = hierarchy;
    }

    @Override
    public void resolveHierarchy() {
        if (resolved) {
            throw new IllegalStateException(String.format(
                    "Signature %s is already resolved",
                    signature
            ));
        }

        String current = signature;
        while (current.contains("L") && current.contains(";")) {
            int start = current.indexOf('L') + 1;
            int end = current.indexOf(';', start);
            int generics = current.indexOf('<', start);
            if (generics != -1 && end > generics)
                end = generics;

            String before = current.substring(0, start);
            String after = current.substring(end);
            String className = current.substring(start, end);

            final KlassNode klassNode = hierarchy.resolveClass(className);
            if (klassNode == null) {
                throw new IllegalStateException(String.format(
                        "Could not find class for signature %s",
                        className
                ));
            }
            classes.add(klassNode);

            resolvedSignature += before;
            resolvedSignature += "%s";
            if (after.indexOf('L') == -1) {
                resolvedSignature += after;
            }

            current = after;
        }
    }

    public boolean isResolved() {
        return resolved;
    }

    public String getOriginalSignature() {
        return signature;
    }

    public String dump() {
        if (!resolved) {
            throw new IllegalStateException(String.format(
                    "Unresolved signature %s",
                    signature
            ));
        }

        if (classes.isEmpty()) {
            return signature;
        }

        final String[] types = classes.stream().map(KlassNode::getName).toArray(String[]::new);
        System.out.printf("OG: %s SIGNATURE: %s TYPES: %s%n", signature, resolvedSignature, Arrays.toString(types));
        return resolvedSignature.formatted((Object[]) types);
    }
}
