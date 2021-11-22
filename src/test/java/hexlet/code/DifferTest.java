package hexlet.code;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DifferTest {

    private String jsonFilename1 = "src/test/resources/file1.json";
    private String jsonFilename2 = "src/test/resources/file2.json";
    private String jsonFilename3 = "src/test/resources/file3.json";
    private String jsonFilename4 = "src/test/resources/file4.json";
    private String notExistingFilepath = "src/test/resources/I_DONT_EXIST.json";
    private String notJsonFilepath = "src/test/resources/notJson.json";
    private String emptyJsonFilepath = "src/test/resources/empty.json";
    private String yamlFilename1 = "src/test/resources/file1.yml";
    private String yamlFilename2 = "src/test/resources/file2.yml";
    private String yamlFilename3 = "src/test/resources/file3.yml";
    private String yamlFilename4 = "src/test/resources/file4.yml";

    @Test
    void cliRunTest() throws Exception {

        String[] args = {jsonFilename1, jsonFilename2};
        String actual = tapSystemOut(() -> {
            int exitCode = new CommandLine(new App()).execute(args);
        });

        String expected = "{\n"
                + "  - follow: false\n"
                + "    host: hexlet.io\n"
                + "  - proxy: 123.234.53.22\n"
                + "  - timeout: 50\n"
                + "  + timeout: 20\n"
                + "  + verbose: true\n"
                + "}\n"; // additional new line from system out
        assertEquals(expected, actual);

    }

    @Test
    void generateTest() throws IOException {
        String actual = Differ.generate(jsonFilename1, jsonFilename2);
        String expected = "{\n"
                + "  - follow: false\n"
                + "    host: hexlet.io\n"
                + "  - proxy: 123.234.53.22\n"
                + "  - timeout: 50\n"
                + "  + timeout: 20\n"
                + "  + verbose: true\n"
                + "}";
        assertEquals(expected, actual);
    }

    @Test
    void readFileExceptionThrownTest() {

        assertThrows(IOException.class, () -> {
            Differ.readFile(notExistingFilepath);
        });

        assertThrows(IOException.class, () -> {
            String data = Differ.readFile(notJsonFilepath);
            Parser.parse(data, "json");
        });
    }

    @Test
    void yamlFilesPlainFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename3, yamlFilename4, "plain");

        String expected = "Property 'chars2' was updated. From [complex value] to false\n"
                + "Property 'checked' was updated. From false to true\n"
                + "Property 'default' was updated. From null to [complex value]\n"
                + "Property 'id' was updated. From 45 to null\n"
                + "Property 'key1' was removed\n"
                + "Property 'key2' was added with value: 'value2'\n"
                + "Property 'numbers2' was updated. From [complex value] to [complex value]\n"
                + "Property 'numbers3' was removed\n"
                + "Property 'numbers4' was added with value: [complex value]\n"
                + "Property 'obj1' was added with value: [complex value]\n"
                + "Property 'setting1' was updated. From 'Some value' to 'Another value'\n"
                + "Property 'setting2' was updated. From 200 to 300\n"
                + "Property 'setting3' was updated. From true to 'none'";
        assertEquals(expected, actual);
    }

    @Test
    void jsonFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(jsonFilename1, jsonFilename2, "stylish");
        String expected = "{\n"
                + "  - follow: false\n"
                + "    host: hexlet.io\n"
                + "  - proxy: 123.234.53.22\n"
                + "  - timeout: 50\n"
                + "  + timeout: 20\n"
                + "  + verbose: true\n"
                + "}";
        assertEquals(expected, actual);
    }

    @Test
    void yamlFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename1, yamlFilename2, "stylish");
        String expected = "{\n"
                + "    calling-birds: four\n"
                + "    count: 1\n"
                + "  - doe: a deer, a female deer\n"
                + "  + french-hens: 3\n"
                + "  - golden-rings: 5\n"
                + "  + golden-rings: 6\n"
                + "    location: a pear tree\n"
                + "    pi: 3.14159\n"
                + "  + ray: a drop of golden sun\n"
                + "  - turtle-doves: two\n"
                + "  + turtle-doves: three\n"
                + "    xmas: true\n"
                + "}";
        assertEquals(expected, actual);
    }

    @Test
    void yamlFilesJsonFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename1, yamlFilename2, "json");
        String expected = Files.readString(new File("src/test/resources/yaml_1_2_expected_diff.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void jsonComplexFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(jsonFilename3, jsonFilename4, "stylish");
        String expected = Files.readString(new File("src/test/resources/json_3_4_expected_diff.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void yamlComplexFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename3, yamlFilename4, "stylish");
        String expected = Files.readString(new File("src/test/resources/json_3_4_expected_diff.txt").toPath());
        assertEquals(expected, actual);
    }
}
