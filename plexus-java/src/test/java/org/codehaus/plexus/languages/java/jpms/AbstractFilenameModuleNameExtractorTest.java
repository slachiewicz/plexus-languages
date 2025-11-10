package org.codehaus.plexus.languages.java.jpms;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class AbstractFilenameModuleNameExtractorTest {
    protected abstract ModuleNameExtractor getExtractor();

    @Test
    void jarWithoutManifest() throws Exception {
        String name = getExtractor().extract(Path.of("src/test/test-data/jar.empty/plexus-java-1.0.0-SNAPSHOT.jar"));
        assertEquals("plexus.java", name);
    }

    @Test
    void jarWithManifest() throws Exception {
        String name = getExtractor()
                .extract(Path.of("src/test/test-data/jar.manifest.with/plexus-java-1.0.0-SNAPSHOT.jar"));
        assertEquals("org.codehaus.plexus.languages.java", name);
    }

    @Test
    void jarUnsupported() throws Exception {
        String name = getExtractor().extract(Path.of("src/test/test-data/jar.unsupported/jdom-1.0.jar"));
        assertNull(name);
    }

    @Test
    void jarWithSpacesInPath() throws Exception {
        String name = getExtractor()
                .extract(Path.of("src/test/test-data/jar with spaces in path/plexus-java-1.0.0-SNAPSHOT.jar"));
        assertEquals("org.codehaus.plexus.languages.java", name);
    }
}
