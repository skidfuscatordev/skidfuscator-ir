package dev.skidfuscator.ir.hierarchy;

public class HierarchyConfig {
    private boolean generatePhantomClasses;
    private boolean generatePhantomMethods;
    private boolean generatePhantomFields;

    public static Builder of() {
        return new Builder();
    }

    public boolean isGeneratePhantomClasses() {
        return generatePhantomClasses;
    }

    public boolean isGeneratePhantomMethods() {
        return generatePhantomMethods;
    }

    public boolean isGeneratePhantomFields() {
        return generatePhantomMethods;
    }

    public static final class Builder {
        private boolean generatePhantomClasses;
        private boolean generatePhantomMethods;
        private boolean generatePhantomFields;

        private Builder() {
        }

        public Builder generatePhantomClasses() {
            this.generatePhantomClasses = true;
            return this;
        }

        public Builder generatePhantomMethods() {
            this.generatePhantomMethods = true;
            return this;
        }

        public Builder generatePhantomFields() {
            this.generatePhantomFields = true;
            return this;
        }

        public HierarchyConfig build() {
            HierarchyConfig hierarchyConfig = new HierarchyConfig();
            hierarchyConfig.generatePhantomMethods = this.generatePhantomMethods;
            hierarchyConfig.generatePhantomClasses = this.generatePhantomClasses;
            hierarchyConfig.generatePhantomFields = this.generatePhantomFields;
            return hierarchyConfig;
        }
    }
}
