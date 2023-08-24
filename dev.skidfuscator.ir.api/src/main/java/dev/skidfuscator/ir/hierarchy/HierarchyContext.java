package dev.skidfuscator.ir.hierarchy;

import dev.skidfuscator.ir.Klass;

public interface HierarchyContext {
    /**
     * Factory method to create a class from
     * a defined factory.
     */
    Klass create(final String name);

    /**
     * @return  Configuration of the hierarchy
     *          context provided
     */
    HierarchyConfig config();
}
