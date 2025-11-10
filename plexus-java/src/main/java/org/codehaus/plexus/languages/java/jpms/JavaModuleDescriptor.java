package org.codehaus.plexus.languages.java.jpms;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

/**
 * Simple representation of a ModuleDescriptor containing info required by this plugin.
 * It will provide only methods matching Java 9 ModuleDescriptor, so once Java 9  is required, we can easily switch
 *
 * @author Robert Scholte
 * @since 1.0.0
 *
 */
public class JavaModuleDescriptor {
    private String name;

    private boolean automatic;

    private Set<JavaRequires> requires = new LinkedHashSet<>();

    private Set<JavaExports> exports = new LinkedHashSet<>();

    private Set<String> uses = new LinkedHashSet<>();

    private Set<JavaProvides> provides = new LinkedHashSet<>();

    public String name() {
        return name;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public Set<JavaRequires> requires() {
        return unmodifiableSet(requires);
    }

    public Set<JavaExports> exports() {
        return unmodifiableSet(exports);
    }

    public Set<JavaProvides> provides() {
        return unmodifiableSet(provides);
    }

    public Set<String> uses() {
        return unmodifiableSet(uses);
    }

    public static JavaModuleDescriptor.Builder newModule(String name) {
        return new Builder(name).setAutomatic(false);
    }

    public static Builder newAutomaticModule(String name) {
        return new Builder(name).setAutomatic(true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, automatic, requires, exports);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        JavaModuleDescriptor other = (JavaModuleDescriptor) obj;
        if (automatic != other.automatic) {
            return false;
        }
        if (!Objects.equals(name, other.name)) {
            return false;
        }
        if (!Objects.equals(requires, other.requires)) {
            return false;
        }
        if (!Objects.equals(exports, other.exports)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JavaModuleDescriptor{" + "name='" +
                name + '\'' + ", automatic=" +
                automatic + ", requires=" +
                requires + ", exports=" +
                exports + ", uses=" +
                uses + ", provides=" +
                provides + '}';
    }

    /**
     * A JavaModuleDescriptor Builder
     *
     * @author Robert Scholte
     * @since 1.0.0
     */
    public static final class Builder {
        private JavaModuleDescriptor jModule;

        private Builder(String name) {
            jModule = new JavaModuleDescriptor();
            jModule.name = name;
        }

        private Builder setAutomatic(boolean isAutomatic) {
            jModule.automatic = isAutomatic;
            return this;
        }

        /**
         * Adds a dependence on a module with the given (and possibly empty) set of modifiers.
         *
         * @param modifiers The set of modifiers
         * @param name The module name
         * @return This builder
         */
        public Builder requires(Set<JavaModuleDescriptor.JavaRequires.JavaModifier> modifiers, String name) {
            jModule.requires.add(new JavaRequires(modifiers, name));
            return this;
        }

        /**
         * Adds a dependence on a module with an empty set of modifiers.
         *
         * @param name The module name
         * @return This builder
         */
        public Builder requires(String name) {
            jModule.requires.add(new JavaRequires(name));
            return this;
        }

        /**
         * Adds an exported package. The package is exported to all modules.
         *
         * @param source The package name
         * @return This builder
         */
        public Builder exports(String source) {
            jModule.exports.add(new JavaExports(source));
            return this;
        }

        /**
         * Adds an exported package. The package is exported to a set of target modules.
         *
         * @param source The package name
         * @param targets  The set of target modules names
         * @return This builder
         */
        public Builder exports(String source, Set<String> targets) {
            jModule.exports.add(new JavaExports(source, targets));
            return this;
        }

        /**
         * Adds a service dependence.
         *
         * @param service The service type
         * @return This Builder
         */
        public Builder uses(String service) {
            jModule.uses.add(service);
            return this;
        }

        public Builder provides(String service, List<String> providers) {
            jModule.provides.add(new JavaProvides(service, providers));
            return this;
        }

        /**
         * Builds and returns a ModuleDescriptor from its components.
         *
         * @return The module descriptor
         */
        public JavaModuleDescriptor build() {
            return jModule;
        }
    }

    /**
     * Represents ModuleDescriptor.Requires
     *
     * @author Robert Scholte
     * @since 1.0.0
     */
    public static class JavaRequires {
        private final Set<JavaModifier> modifiers;

        private final String name;

        private JavaRequires(Set<JavaModifier> modifiers, String name) {
            this.modifiers = modifiers;
            this.name = name;
        }

        private JavaRequires(String name) {
            this.modifiers = emptySet();
            this.name = name;
        }

        public Set<JavaModifier> modifiers() {
            return modifiers;
        }

        public String name() {
            return name;
        }

        /**
         * Represents ModuleDescriptor.Requires.Modifier
         *
         * @author Robert Scholte
         * @since 1.0.0
         */
        public enum JavaModifier {
            STATIC,
            TRANSITIVE
        }

        @Override
        public int hashCode() {
            return Objects.hash(modifiers, name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            JavaRequires other = (JavaRequires) obj;
            if (!Objects.equals(modifiers, other.modifiers)) {
                return false;
            }
            if (!Objects.equals(name, other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "JavaRequires{" + "modifiers=" + modifiers + ", name='" + name + '\'' + '}';
        }
    }

    /**
     * Represents ModuleDescriptor.Exports
     *
     * @author Robert Scholte
     * @since 1.0.0
     */
    public static class JavaExports {
        private final String source;

        private final Set<String> targets;

        private JavaExports(String source) {
            this.source = source;
            this.targets = null;
        }

        public JavaExports(String source, Set<String> targets) {
            this.source = source;
            this.targets = targets;
        }

        public String source() {
            return source;
        }

        public Set<String> targets() {
            return targets;
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, targets);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            JavaExports other = (JavaExports) obj;
            if (!Objects.equals(source, other.source)) {
                return false;
            }
            if (!Objects.equals(targets, other.targets)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "JavaExports{" + "source='" + source + '\'' + ", targets=" + targets + '}';
        }
    }

    /**
     * Represents ModuleDescriptor.Provides
     *
     * @author Robert Scholte
     * @since 1.0.0
     */
    public static class JavaProvides {
        private final String service;

        private final List<String> providers;

        private JavaProvides(String service, List<String> providers) {
            this.service = service;
            this.providers = providers;
        }

        public String service() {
            return service;
        }

        public List<String> providers() {
            return providers;
        }

        @Override
        public int hashCode() {
            return Objects.hash(service, providers);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            JavaProvides other = (JavaProvides) obj;
            if (!Objects.equals(service, other.service)) {
                return false;
            }
            if (!Objects.equals(providers, other.providers)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "JavaProvides{" + "service='" + service + '\'' + ", providers=" + providers + '}';
        }
    }
}
