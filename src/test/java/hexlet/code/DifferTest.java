package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.formatter.PlainFormatter;
import hexlet.code.formatter.StylishFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DifferTest {

    private String jsonFilename1;
    private String jsonFilename2;
    private String jsonFilename3;
    private String jsonFilename4;
    private String notExistingFilepath;
    private String notJsonFilepath;
    private String emptyJsonFilepath;
    private String jsonStringFile1;
    private String yamlFilename1;
    private String yamlFilename2;
    private String yamlFilename3;
    private String yamlFilename4;
    private String yamlStringFile1;
    private String differFiles3_4;

    private File jsonFile1;
    private File jsonFile2;
    private File jsonFile3;
    private File jsonFile4;
    private File notExistingFile;
    private File notJsonFile;
    private File emptyJsonFile;
    private File yamlFile1;
    private File yamlFile2;
    private File yamlFile3;
    private File yamlFile4;

    private StylishFormatter stylishFormatter = new StylishFormatter();
    private PlainFormatter plainFormatter = new PlainFormatter();

    @BeforeEach
    void setup() {
        jsonFilename1 = "src/test/resources/file1.json";
        jsonFilename2 = "src/test/resources/file2.json";
        jsonFilename3 = "src/test/resources/file3.json";
        jsonFilename4 = "src/test/resources/file4.json";
        notExistingFilepath = "src/test/resources/I_DONT_EXIST.json";
        notJsonFilepath = "src/test/resources/notJson.json";
        emptyJsonFilepath = "src/test/resources/empty.json";
        yamlFilename1 = "src/test/resources/file1.yml";
        yamlFilename2 = "src/test/resources/file2.yml";
        yamlFilename3 = "src/test/resources/file3.yml";
        yamlFilename4 = "src/test/resources/file4.yml";

        jsonFile1 = Differ.getFileObj(jsonFilename1);
        jsonFile2 = Differ.getFileObj(jsonFilename2);
        jsonFile3 = Differ.getFileObj(jsonFilename3);
        jsonFile4 = Differ.getFileObj(jsonFilename4);
        notExistingFile = Differ.getFileObj(notExistingFilepath);
        notJsonFile = Differ.getFileObj(notJsonFilepath);
        emptyJsonFile = Differ.getFileObj(emptyJsonFilepath);
        yamlFile1 = Differ.getFileObj(yamlFilename1);
        yamlFile2 = Differ.getFileObj(yamlFilename2);
        yamlFile3 = Differ.getFileObj(yamlFilename3);
        yamlFile4 = Differ.getFileObj(yamlFilename4);


        jsonStringFile1 = "{ \"host\" : \"hexlet.io\", \"timeout\" : 50, \"proxy\" : \"123.234.53.22\", " +
                "\"follow\": false }";

        yamlStringFile1 = "{\"doe\": \"a deer, a female deer\", " +
                "\"pi\": 3.14159," +
                "\"xmas\": true," +
                "\"calling-birds\": \"four\"," +
                "\"golden-rings\": 5, " +
                "\"count\": 1," +
                "\"location\": \"a pear tree\"" +
                "\"turtle-doves\": \"two\" " +
                "}";

        differFiles3_4 = "{\n" +
                "    chars1: [a, b, c]\n" +
                "  - chars2: [d, e, f]\n" +
                "  + chars2: false\n" +
                "  - checked: false\n" +
                "  + checked: true\n" +
                "  - default: null\n" +
                "  + default: [value1, value2]\n" +
                "  - id: 45\n" +
                "  + id: null\n" +
                "  - key1: value1\n" +
                "  + key2: value2\n" +
                "    numbers1: [1, 2, 3, 4]\n" +
                "  - numbers2: [2, 3, 4, 5]\n" +
                "  + numbers2: [22, 33, 44, 55]\n" +
                "  - numbers3: [3, 4, 5]\n" +
                "  + numbers4: [4, 5, 6]\n" +
                "  + obj1: {nestedKey=value, isNested=true}\n" +
                "  - setting1: Some value\n" +
                "  + setting1: Another value\n" +
                "  - setting2: 200\n" +
                "  + setting2: 300\n" +
                "  - setting3: true\n" +
                "  + setting3: none\n" +
                "}";
    }

    // region getFileObj Test
    @Test
    void getFileObj_RelativePathTest() {
        String absFilepath = System.getProperty("user.dir") + "/" + jsonFilename1;
        File expected = new File(absFilepath);
        assertEquals(expected, jsonFile1);
    }

    @Test
    void getFileObj_FileExistsTest() {
        assertFalse(notExistingFile.exists());

        File file1 = Differ.getFileObj(jsonFilename1);
        assertTrue(file1.exists());
    }

    // endregion

    // region getDataFromFile
    @Test
    void getDataFromFile_JsonFile1Test() throws IOException {
        Map<String, String> actual = Parser.getDataFromFile(jsonFile1);

        TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};
        Map<String, String> expected = new ObjectMapper().readValue(jsonStringFile1, typeRef);
        assertEquals(expected, actual);

        Map<String, String> JsonFile2Data = Parser.getDataFromFile(jsonFile2);
        assertNotEquals(actual, JsonFile2Data);
    }

    @Test
    void getDataFromFile_ExceptionThrownTest() {
        assertThrows(IOException.class, () -> {
            Parser.getDataFromFile(notExistingFile);
        });

        assertThrows(IOException.class, () -> {
            Parser.getDataFromFile(notJsonFile);
        });

        assertThrows(IOException.class, () -> {
            Parser.getDataFromFile(emptyJsonFile);
        });
    }

    // endregion

    // region getFields
    @Test
    void getFields_FromJsonTest() throws IOException {
        Map<String, String> jsonFile1Data = Parser.getDataFromFile(jsonFile1);
        Set<String> actual = Parser.getFields(jsonFile1Data);

        Set<String> expected = Set.of("host", "timeout", "proxy", "follow");
        assertEquals(expected, actual, "Wrong set of keys");
    }

    @Test
    void getFields_FromYamlTest() throws IOException {
        Map<String, String> yamlFile1Data = Parser.getDataFromFile(yamlFile1);
        Set<String> actual = Parser.getFields(yamlFile1Data);

        Set<String> expected = Set.of("doe", "pi", "xmas", "calling-birds",
                "golden-rings", "count", "location", "turtle-doves");
        assertEquals(expected, actual, "Wrong set of keys");
    }

    // endregion

    // region PlainFormatter Test
    @Test
    void diffsListPlainFormatter_Test() throws IOException {
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile3);
        JsonNode yamlFile2Data = Parser.getNodeDataFromFile(yamlFile4);

        List<Diff> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = plainFormatter.formatDiffsList(diffList);

        String expected = "Property 'chars2' was updated. From [complex value] to false\n" +
                "Property 'checked' was updated. From false to true\n" +
                "Property 'default' was updated. From null to [complex value]\n" +
                "Property 'id' was updated. From 45 to null\n" +
                "Property 'key1' was removed\n" +
                "Property 'key2' was added with value: 'value2'\n" +
                "Property 'numbers2' was updated. From [complex value] to [complex value]\n" +
                "Property 'numbers3' was removed\n" +
                "Property 'numbers4' was added with value: [complex value]\n" +
                "Property 'obj1' was added with value: [complex value]\n" +
                "Property 'setting1' was updated. From 'Some value' to 'Another value'\n" +
                "Property 'setting2' was updated. From 200 to 300\n" +
                "Property 'setting3' was updated. From true to 'none'\n";
        assertEquals(expected, actual);
    }

    // endregion

    // region diffsListToString Test
    @Test
    void diffsListStylishFormatter_JsonTest() throws IOException {
        JsonNode jsonFile1Data = Parser.getNodeDataFromFile(jsonFile1);
        JsonNode jsonFile2Data = Parser.getNodeDataFromFile(jsonFile2);

        List<Diff> diffList = Differ.getDiff(jsonFile1Data, jsonFile2Data);
        String actual = stylishFormatter.formatDiffsList(diffList);
        String expected = "{\n" +
                "  - follow: false\n" +
                "    host: hexlet.io\n" +
                "  - proxy: 123.234.53.22\n" +
                "  - timeout: 50\n" +
                "  + timeout: 20\n" +
                "  + verbose: true\n" +
                "}";
        assertEquals(expected, actual);
    }

    @Test
    void diffsListStylishFormatter_YamlTest() throws IOException {
        JsonNode yamlFile1Data = Parser.getNodeDataFromFile(yamlFile1);
        JsonNode yamlFile2Data = Parser.getNodeDataFromFile(yamlFile2);

        List<Diff> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = stylishFormatter.formatDiffsList(diffList);
        String expected = "{\n" +
                "    calling-birds: four\n" +
                "    count: 1\n" +
                "  - doe: a deer, a female deer\n" +
                "  + french-hens: 3\n" +
                "  - golden-rings: 5\n" +
                "  + golden-rings: 6\n" +
                "    location: a pear tree\n" +
                "    pi: 3.14159\n" +
                "  + ray: a drop of golden sun\n" +
                "  - turtle-doves: two\n" +
                "  + turtle-doves: three\n" +
                "    xmas: true\n" +
                "}";
        assertEquals(expected, actual);
    }

    // endregions

    @Test
    void getNodeDataFromFile_ComplexJsonTest() throws IOException {
        JsonNode node3 = Parser.getNodeDataFromFile(jsonFile3);
        JsonNode node4 = Parser.getNodeDataFromFile(jsonFile4);

        List<Diff> diff = Differ.getDiff(node3, node4);
        String diffString = stylishFormatter.formatDiffsList(diff);

        assertEquals(differFiles3_4, diffString);
    }

    @Test
    void getNodeDataFromFile_ComplexYmlTest() throws IOException {
        JsonNode node3 = Parser.getNodeDataFromFile(yamlFile3);
        JsonNode node4 = Parser.getNodeDataFromFile(yamlFile4);

        List<Diff> diff = Differ.getDiff(node3, node4);
        String diffString = stylishFormatter.formatDiffsList(diff);

        assertEquals(differFiles3_4, diffString);
    }
}
