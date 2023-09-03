package dev.skidfuscator.ir.hierarchy;


import dev.skidfuscator.ir.Klass;

import java.util.Collection;

public interface Hierarchy {
    /**
     * @return
     */
    HierarchyConfig getConfig();

    /**
     * Iterates over all classes in the hierarchy
     *
     * @return the iterable of classes
     */
    Iterable<Klass> iterateKlasses();

    /**
     * Resolve classes in a typical BFS top down strategy.
     * This allows for the construction of method groups
     * based on their parents, making it easier to have
     * less conflicts for branches for methods.
     *
     * @param classes Collection of classes to resolve
     */
    void resolveClasses(final Collection<Klass> classes);

    /**
     * Finds a class with the given name, if the
     * class is not found, a blank placeholder will
     * be created.
     *
     * @param name the name of the class to find
     * @return the KlassNode object representing the found class
     */
    Klass resolveClass(final String name);

}
