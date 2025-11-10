package org.codehaus.plexus.languages.java.jpms;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManifestModuleNameExtractorTest {
    private ManifestModuleNameExtractor extractor = new ManifestModuleNameExtractor();

    @Test
    void noManifestInJar() throws Exception {
        assertNull(extractor.extract(Path.of("src/test/test-data/jar.name/plexus-java-1.0.0-SNAPSHOT.jar")));
    }

    @Test
    void manifestInJar() throws Exception {
        assertEquals(
                "org.codehaus.plexus.languages.java",
                extractor.extract(Path.of("src/test/test-data/jar.manifest.with/plexus-java-1.0.0-SNAPSHOT.jar")));
    }

    @Test
    void noManifestInDir() throws Exception {
        assertNull(extractor.extract(Path.of("src/test/test-data/empty/out")));
    }

    @Test
    void emptyManifestInDir() throws Exception {
        assertNull(extractor.extract(Path.of("src/test/test-data/manifest.without/out")));
    }

    @Test
    void manifestInDir() throws Exception {
        assertEquals("auto.by.manifest", extractor.extract(Path.of("src/test/test-data/dir.manifest.with/out")));
    }
}
