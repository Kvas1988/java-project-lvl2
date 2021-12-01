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

    private String jsonFilename1 = getResourceFile("file1.json").getAbsolutePath();
    private String jsonFilename2 = getResourceFile("file2.json").getAbsolutePath();
    private String jsonFilename3 = getResourceFile("file3.json").getAbsolutePath();
    private String jsonFilename4 = getResourceFile("file4.json").getAbsolutePath();
    private String notJsonFilepath = getResourceFile("notJson.json").getAbsolutePath();
    private String yamlFilename1 = getResourceFile("file1.yml").getAbsolutePath();
    private String yamlFilename2 = getResourceFile("file2.yml").getAbsolutePath();
    private String yamlFilename3 = getResourceFile("file3.yml").getAbsolutePath();
    private String yamlFilename4 = getResourceFile("file4.yml").getAbsolutePath();
    private String notExistingFilepath = "src/test/resources/I_DONT_EXIST.json";

    private File getResourceFile(String filename) {
        // https://mkyong.com/java/java-read-a-file-from-resources-folder/
        return new File(getClass().getClassLoader().getResource(filename).getFile());
    }

    @Test
    void cliRunTest() throws Exception {

        String[] args = {jsonFilename1, jsonFilename2};
        String actual = tapSystemOut(() -> {
            int exitCode = new CommandLine(new App()).execute(args);
        });
        String expected = Files.readString(getResourceFile("expected_json1_json2_stylish.txt").toPath());
        expected += "\n";
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
        String expected = Files.readString(getResourceFile("expected_yml3_yml4_plain.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void jsonFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(jsonFilename1, jsonFilename2, "stylish");
        String expected = Files.readString(getResourceFile("expected_json1_json2_stylish.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void yamlFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename1, yamlFilename2, "stylish");
        String expected = Files.readString(getResourceFile("expected_yml1_yml2_stylish.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void yamlFilesJsonFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename1, yamlFilename2, "json");
        String expected = Files.readString(getResourceFile("expected_yml1_yml2_json.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void jsonComplexFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(jsonFilename3, jsonFilename4, "stylish");
        String expected = Files.readString(getResourceFile("expected_json3_json4_stylish.txt").toPath());
        assertEquals(expected, actual);
    }

    @Test
    void yamlComplexFilesStylishFormatterTest() throws IOException {
        String actual = Differ.generate(yamlFilename3, yamlFilename4, "stylish");
        String expected = Files.readString(getResourceFile("expected_json3_json4_stylish.txt").toPath());
        assertEquals(expected, actual);
    }
}
