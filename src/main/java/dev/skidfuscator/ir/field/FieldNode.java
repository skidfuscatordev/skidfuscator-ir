package dev.skidfuscator.ir.field;

import dev.skidfuscator.ir.hierarchy.HierarchyResolvable;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.type.TypeWrapper;

//TODO: Different name due to ASM FieldNode
public interface FieldNode extends HierarchyResolvable {

    /**
     * Resolves the field
     */
    void resolve();

    /**
     * Method used to dump the information from
     * the field node. Currently not implemented.
     */
    void dump();

    /**
     * Retrieves the parent class of the field.
     *
     * @return The parent class of the field
     */
    KlassNode getParent();

    /**
     * Sets the parent class of the field.
     *
     * @param node The parent class to set
     */
    void setParent(final KlassNode node);

    /**
     * Retrieves the name of the field.
     *
     * @return The name of the field
     */
    String getName();

    /**
     * Sets the name of the field.
     *
     * @param name The name to set
     */
    void setName(final String name);

    /**
     * Retrieves the descriptor of the field.
     *
     * @return The descriptor of the field
     */
    TypeWrapper getDesc();

    /**
     * Determines if the field is static.
     *
     * @return True if the field is static, false otherwise
     */
    boolean isStatic();
}
