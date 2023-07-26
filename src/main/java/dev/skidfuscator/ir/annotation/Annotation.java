package dev.skidfuscator.ir.annotation;

import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.util.Parameter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Annotation {
    private final Hierarchy hierarchy;
    private final AnnotationNode node;
    private final AnnotationType type;
    private transient KlassNode owner;
    private final Map<String, AnnotationValue<?>> values = new HashMap<>();

    public Annotation(Hierarchy hierarchy, AnnotationNode node, AnnotationType type) {
        this.hierarchy = hierarchy;
        this.node = node;
        this.type = type;
    }

    /**
     * @param name Name of the value sought out to be modified (eg @Value(value = "123) )
     *                                                                    ^^^^^ this bit
     * @param <T> Type of the value sought out to be modified
     * @return Annotation Value subclass with the getter and the setter
     */
    public <T> AnnotationValue<T> getValue(String name) {
        return (AnnotationValue) values.get(name);
    }

    /**
     * @param name Name of the value sought out to be modified (eg @Value(value = "123) )
     *                                                                    ^^^^^ this bit
     * @param <T> Type of the value sought out to be modified
     * @return Annotation Value subclass with the getter and the setter
     */
    public <T> void setValue(String name, T value) {
        int i;

        if (values.containsKey(name)) {
            i = values.get(name).index;
        } else {
            i = values.size();
        }

        final int finalI = i;
        final String finalName = name;
        final AnnotationValue<T> array = new AnnotationValue<>(
                name,
                finalI,
                o -> node.values.set(finalI, o),
                () -> (T) node.values.get(finalI),
                owner.getMethods()
                        .stream()
                        .filter(e -> e.getName().equals(finalName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "Failed to find method for "
                                        + finalName + " of value "
                                        + node.values.get(finalI)
                                        + " (parent: " + owner.getMethods().stream()
                                        .map(e -> e.getName() + "#" + e.getDesc())
                                        .collect(Collectors.joining("\n"))
                                        + ")"
                        )));
        array.set(value);
        values.put(name, array);
    }

    private void updateDesc() {
        if (values.size() == 1) {
            node.desc = null;
            return;
        }

        final Parameter parameter = new Parameter("()V");
        values.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().index))
                .collect(Collectors.toList());
    }

    /**
     * @return Annotation ASM node to be used
     */
    public AnnotationNode getNode() {
        return node;
    }

    /**
     * @return Annotation ASM type for debugging purposes (need to inform myself on the difference)
     */
    public AnnotationType getType() {
        return type;
    }

    /**
     * @return Value map with all the values and their headings
     */
    public Map<String, AnnotationValue<?>> getValues() {
        return values;
    }

    /**
     * @return Parent class which defines the annotation
     */
    public KlassNode getOwner() {
        return owner;
    }

    /**
     * Function which serves the purpose of parsing an annotation into values that
     * can directly virtually edit the annotation
     */
    public void resolve() {
        this.owner = hierarchy.findClass(Type.getType(node.desc).getClassName().replace(".", "/"));

        if (node.values == null || node.values.size() == 0) {
            return;
        }

        String name = null;
        if (node.desc == null) {
            values.put("value", new AnnotationValue<>(
                    "value",
                    0,
                    o -> node.values.set(0, o),
                    () -> node.values.get(0),
                    owner.getMethods().iterator().next()
            ));
            return;
        }

        for (int i = 0; i < this.node.values.size(); i++) {
            if (i % 2 == 0) {
                // This is the name
                name = (String) node.values.get(i);
            } else {
                final int finalI = i;
                String finalName = name;
                values.put(name, new AnnotationValue<>(
                        name,
                        finalI,
                        o -> node.values.set(finalI, o),
                        () -> node.values.get(finalI),
                        owner.getMethods()
                                .stream()
                                .filter(e -> e.getName().equals(finalName))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                        "Failed to find method for "
                                                + finalName + " of value "
                                                + node.values.get(finalI)
                                                + " (parent: " + owner.getMethods().stream()
                                                .map(e -> e.getName() + "#" + e.getDesc())
                                                .collect(Collectors.joining("\n"))
                                                + ")"
                                ))
                ));
            }
        }
    }

    public static class AnnotationValue<T> {
        private final String name;

        private final int index;
        private final Type type;
        private final Consumer<T> setter;
        private final Supplier<T> getter;
        private final FunctionNode methodNode;

        public AnnotationValue(String name, int index, Consumer<T> setter, Supplier<T> getter, FunctionNode methodNode) {
            this.name = name;
            this.index = index;
            this.setter = setter;
            this.getter = getter;
            this.methodNode = methodNode;
            this.type = get() == null ? Type.getType(String.class) : Type.getType(get().getClass());
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public Type getType() {
            return type;
        }

        public FunctionNode getMethodNode() {
            return methodNode;
        }

        public void set(final T t) {
            setter.accept(t);
        }

        public T get() {
            return getter.get();
        }
    }

    public enum AnnotationType {
        VISIBLE(false),
        INVISIBLE(false),
        TYPE_VISIBLE(true),
        TYPE_INVISIBLE(true);

        private final boolean type;

        AnnotationType(boolean type) {
            this.type = type;
        }

        public boolean isType() {
            return type;
        }
    }
}
