package io.runtoolkit.datapackfixer;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DatapackSyntaxScannerTest {
    @Test void reportsInvalidJsonWithoutWritingFiles() throws Exception {
        Path root = Files.createTempDirectory("dfx");
        Path file = root.resolve("sample/data/example/recipe/broken.json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "{ \"type\": ");
        var diagnostics = new DatapackSyntaxScanner().scan(root);
        assertTrue(diagnostics.stream().anyMatch(d -> d.code().equals("JSON_INVALID")));
        assertEquals("{ \"type\": ", Files.readString(file));
    }
    @Test void reports26_2MigrationAndLegacyDirectory() throws Exception {
        Path root = Files.createTempDirectory("dfx");
        Path file = root.resolve("sample/data/example/predicates/slime.json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "{\"type\":\"minecraft:type_specific/slime\"}");
        var diagnostics = new DatapackSyntaxScanner().scan(root);
        assertTrue(diagnostics.stream().anyMatch(d -> d.code().equals("LEGACY_DIRECTORY")));
        assertTrue(diagnostics.stream().anyMatch(d -> d.code().equals("TYPE_SPECIFIC_SLIME")));
    }
    @Test void detectsMalformedFunctionDelimiters() throws Exception {
        Path root = Files.createTempDirectory("dfx");
        Path file = root.resolve("sample/data/example/function/load.mcfunction");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "give @s minecraft:stone[custom_name='broken'");
        assertTrue(new DatapackSyntaxScanner().scan(root).stream().anyMatch(d -> d.code().equals("FUNCTION_DELIMITER")));
    }
}
