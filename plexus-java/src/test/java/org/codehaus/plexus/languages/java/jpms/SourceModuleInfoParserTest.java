package org.codehaus.plexus.languages.java.jpms;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.JavaExports;
import org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.JavaProvides;
import org.codehaus.plexus.languages.java.jpms.JavaModuleDescriptor.JavaRequires;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SourceModuleInfoParserTest {
    private final SourceModuleInfoParser parser = new SourceModuleInfoParser();

    @Test
    void test() throws Exception {
        JavaModuleDescriptor moduleDescriptor =
                parser.fromSourcePath(Path.of("src/test/test-data/src.dir/module-info.java"));
        assertEquals("a.b.c", moduleDescriptor.name());

        Iterator<JavaRequires> requiresIter = moduleDescriptor.requires().iterator();

        JavaRequires requires = requiresIter.next();
        assertEquals("d.e", requires.name());
        assertFalse(requires.modifiers().contains(JavaRequires.JavaModifier.STATIC));
        assertFalse(requires.modifiers().contains(JavaRequires.JavaModifier.TRANSITIVE));

        requires = requiresIter.next();
        assertEquals("s.d.e", requires.name());
        assertTrue(requires.modifiers().contains(JavaRequires.JavaModifier.STATIC));
        assertFalse(requires.modifiers().contains(JavaRequires.JavaModifier.TRANSITIVE));

        requires = requiresIter.next();
        assertEquals("t.d.e", requires.name());
        assertFalse(requires.modifiers().contains(JavaRequires.JavaModifier.STATIC));
        assertTrue(requires.modifiers().contains(JavaRequires.JavaModifier.TRANSITIVE));

        requires = requiresIter.next();
        assertEquals("s.t.d.e", requires.name());
        assertTrue(requires.modifiers().contains(JavaRequires.JavaModifier.STATIC));
        assertTrue(requires.modifiers().contains(JavaRequires.JavaModifier.TRANSITIVE));

        Iterator<JavaExports> exportsIter = moduleDescriptor.exports().iterator();

        JavaExports exports = exportsIter.next();
        assertEquals("f.g", exports.source());

        exports = exportsIter.next();
        assertEquals("f.g.h", exports.source());
        assertEquals(new HashSet<>(Arrays.asList("i.j", "k.l.m")), exports.targets());

        Set<String> uses = moduleDescriptor.uses();
        assertArrayEquals(new String[] {"com.example.foo.spi.Intf"}, uses.toArray(new String[0]));

        Iterator<JavaProvides> providesIter = moduleDescriptor.provides().iterator();
        JavaProvides provides = providesIter.next();

        assertEquals("com.example.foo.spi.Intf", provides.service());
        assertArrayEquals(
                new String[] {"com.example.foo.Impl"}, provides.providers().toArray(new String[0]));
    }
}
