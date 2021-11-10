package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.formatter.JsonFormatter;
import picocli.CommandLine;
import hexlet.code.formatter.PlainFormatter;
import hexlet.code.formatter.StylishFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DifferTest {

    // TODO: test generate

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

    private File jsonFile1 = Differ.getFileObj(jsonFilename1);
    private File jsonFile2 = Differ.getFileObj(jsonFilename2);
    private File jsonFile3 = Differ.getFileObj(jsonFilename3);
    private File jsonFile4 = Differ.getFileObj(jsonFilename4);
    private File notExistingFile = Differ.getFileObj(notExistingFilepath);
    private File notJsonFile = Differ.getFileObj(notJsonFilepath);
    private File emptyJsonFile = Differ.getFileObj(emptyJsonFilepath);
    private File yamlFile1 = Differ.getFileObj(yamlFilename1);
    private File yamlFile2 = Differ.getFileObj(yamlFilename2);
    private File yamlFile3 = Differ.getFileObj(yamlFilename3);
    private File yamlFile4 = Differ.getFileObj(yamlFilename4);

    private StylishFormatter stylishFormatter = new StylishFormatter();
    private PlainFormatter plainFormatter = new PlainFormatter();
    private JsonFormatter jsonFormatter = new JsonFormatter();

    // private String differFiles3And4 = "{\n"
    //             + "    chars1: [a, b, c]\n"
    //             + "  - chars2: [d, e, f]\n"
    //             + "  + chars2: false\n"
    //             + "  - checked: false\n"
    //             + "  + checked: true\n"
    //             + "  - default: null\n"
    //             + "  + default: [value1, value2]\n"
    //             + "  - id: 45\n"
    //             + "  + id: null\n"
    //             + "  - key1: value1\n"
    //             + "  + key2: value2\n"
    //             + "    numbers1: [1, 2, 3, 4]\n"
    //             + "  - numbers2: [2, 3, 4, 5]\n"
    //             + "  + numbers2: [22, 33, 44, 55]\n"
    //             + "  - numbers3: [3, 4, 5]\n"
    //             + "  + numbers4: [4, 5, 6]\n"
    //             + "  + obj1: {nestedKey=value, isNested=true}\n"
    //             + "  - setting1: Some value\n"
    //             + "  + setting1: Another value\n"
    //             + "  - setting2: 200\n"
    //             + "  + setting2: 300\n"
    //             + "  - setting3: true\n"
    //             + "  + setting3: none\n"
    //             + "}";

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
    void generateTest() throws IOException{
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

    // region getFileObj Test
    @Test
    void getFileObjRelativePathTest() {
        String absFilepath = System.getProperty("user.dir") + "/" + jsonFilename1;
        File expected = new File(absFilepath);
        assertEquals(expected, jsonFile1);
    }

    @Test
    void getFileObjFileExistsTest() {
        assertFalse(notExistingFile.exists());

        File file1 = Differ.getFileObj(jsonFilename1);
        assertTrue(file1.exists());
    }

    // endregion

    // region getDataFromFile
    @Test
    void getDataFromFileJsonFile1Test() throws IOException {
        JsonNode actual = Parser.getNodeDataFromFile(jsonFile1);

        String jsonStringFile1 = "{ \"host\" : \"hexlet.io\", \"timeout\" : 50, \"proxy\" : \"123.234.53.22\", "
                + "\"follow\": false }";
        JsonNode expected = new ObjectMapper().readTree(jsonStringFile1);
        assertEquals(expected, actual);

        JsonNode jsonFile2Data = Parser.getNodeDataFromFile(jsonFile2);
        assertNotEquals(actual, jsonFile2Data);
    }

    @Test
    void getDataFromFileExceptionThrownTest() {
        assertThrows(IOException.class, () -> {
            Parser.getNodeDataFromFile(notExistingFile);
        });

        assertThrows(IOException.class, () -> {
            Parser.getNodeDataFromFile(notJsonFile);
        });
    }

    // endregion

    // region getFields
    @Test
    void getFieldsFromJsonTest() throws IOException {
        JsonNode jsonFile1Data = Parser.getNodeDataFromFile(jsonFile1);
        Set<String> actual = Parser.getFields(jsonFile1Data);

        Set<String> expected = Set.of("host", "timeout", "proxy", "follow");
        assertEquals(expected, actual, "Wrong set of keys");
    }

    @Test
    void getFieldsFromYamlTest() throws IOException {
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile1);
        Set<String> actual = Parser.getFields(yamlFile1Data);

        Set<String> expected = Set.of("doe", "pi", "xmas", "calling-birds",
                "golden-rings", "count", "location", "turtle-doves");
        assertEquals(expected, actual, "Wrong set of keys");
    }

    // endregion

    // region PlainFormatter Test
    @Test
    void diffsListPlainFormatterTest() throws IOException {
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile3);
        JsonNode yamlFile2Data = Parser.getNodeDataFromFile(yamlFile4);

        List<Diff> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = plainFormatter.formatDiffsList(diffList);

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

    // endregion

    // region stylish format Test
    @Test
    void diffsListStylishFormatterJsonTest() throws IOException {
        JsonNode jsonFile1Data = Parser.getNodeDataFromFile(jsonFile1);
        JsonNode jsonFile2Data = Parser.getNodeDataFromFile(jsonFile2);

        List<Diff> diffList = Differ.getDiff(jsonFile1Data, jsonFile2Data);
        String actual = stylishFormatter.formatDiffsList(diffList);
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
    void diffsListStylishFormatterYamlTest() throws IOException {
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile1);
        JsonNode yamlFile2Data = Parser.getNodeDataFromFile(yamlFile2);

        List<Diff> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = stylishFormatter.formatDiffsList(diffList);
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

    // endregion

    // region json formatter
    @Test
    void jsonFormatterYamlTest() throws IOException{
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile1);
        JsonNode yamlFile2Data = Parser.getNodeDataFromFile(yamlFile2);

        List<Diff> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = jsonFormatter.formatDiffsList(diffList);
        String expected = Files.readString(new File("src/test/resources/yaml_1_2_expected_diff.txt").toPath());
        assertEquals(expected, actual);
    }
    // endregion

    @Test
    void getNodeDataFromFileComplexJsonTest() throws IOException {
        JsonNode node3 = Parser.getNodeDataFromFile(jsonFile3);
        JsonNode node4 = Parser.getNodeDataFromFile(jsonFile4);

        List<Diff> diff = Differ.getDiff(node3, node4);
        String diffString = stylishFormatter.formatDiffsList(diff);

        String expected = Files.readString(new File("src/test/resources/json_3_4_expected_diff.txt").toPath());
        assertEquals(expected, diffString);
    }

    @Test
    void getNodeDataFromFileComplexYmlTest() throws IOException {
        JsonNode node3 = Parser.getNodeDataFromFile(yamlFile3);
        JsonNode node4 = Parser.getNodeDataFromFile(yamlFile4);

        List<Diff> diff = Differ.getDiff(node3, node4);
        String diffString = stylishFormatter.formatDiffsList(diff);

        String expected = Files.readString(new File("src/test/resources/json_3_4_expected_diff.txt").toPath());
        assertEquals(expected, diffString);
    }
}
