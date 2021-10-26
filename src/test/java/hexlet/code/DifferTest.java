package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DifferTest {

    private String jsonFilename1;
    private String jsonFilename2;
    private String notExistingFilepath;
    private String notJsonFilepath;
    private String emptyJsonFilepath;
    private String jsonStringFile1;
    private String yamlFilename1;
    private String yamlFilename2;
    private String yamlStringFile1;

    private File jsonFile1;
    private File jsonFile2;
    private File notExistingFile;
    private File notJsonFile;
    private File emptyJsonFile;
    private File yamlFile1;
    private File yamlFile2;

    @BeforeEach
    void setup() {
        jsonFilename1 = "src/test/resources/file1.json";
        jsonFilename2 = "src/test/resources/file2.json";
        notExistingFilepath = "src/test/resources/I_DONT_EXIST.json";
        notJsonFilepath = "src/test/resources/notJson.json";
        emptyJsonFilepath = "src/test/resources/empty.json";
        yamlFilename1 = "src/test/resources/file1.yml";
        yamlFilename2 = "src/test/resources/file2.yml";

        jsonFile1 = Differ.getFileObj(jsonFilename1);
        jsonFile2 = Differ.getFileObj(jsonFilename2);
        notExistingFile = Differ.getFileObj(notExistingFilepath);
        notJsonFile = Differ.getFileObj(notJsonFilepath);
        emptyJsonFile = Differ.getFileObj(emptyJsonFilepath);
        yamlFile1 = Differ.getFileObj(yamlFilename1);
        yamlFile2 = Differ.getFileObj(yamlFilename2);


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

    // region getDiff Test
    @Test
    void getDiff_JsonTest() throws IOException {
        Map<String, String> jsonFile1Data = Parser.getDataFromFile(jsonFile1);
        Map<String, String> jsonFile2Data = Parser.getDataFromFile(jsonFile2);

        List<String> actual = Differ.getDiff(jsonFile1Data, jsonFile2Data);
        List<String> expected = List.of("  - follow: false",
                "    host: hexlet.io",
                "  - proxy: 123.234.53.22",
                "  - timeout: 50",
                "  + timeout: 20",
                "  + verbose: true"
                );
        assertEquals(expected, actual);
    }

    @Test
    void getDiff_YamlTest() throws IOException {
        Map<String, String> yamlFile1Data = Parser.getDataFromFile(yamlFile1);
        Map<String, String> yamlFile2Data = Parser.getDataFromFile(yamlFile2);

        List<String> actual = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        List<String> expected = List.of(
                "    calling-birds: four",
                "    count: 1",
                "  - doe: a deer, a female deer",
                "  + french-hens: 3",
                "  - golden-rings: 5",
                "  + golden-rings: 6",
                "    location: a pear tree",
                "    pi: 3.14159",
                "  + ray: a drop of golden sun",
                "  - turtle-doves: two",
                "  + turtle-doves: three",
                "    xmas: true"
        );
        assertEquals(expected, actual);
    }

    // endregion

    // region diffsListToString Test
    @Test
    void diffsListToString_JsonTest() throws IOException {
        Map<String, String> jsonFile1Data = Parser.getDataFromFile(jsonFile1);
        Map<String, String> jsonFile2Data = Parser.getDataFromFile(jsonFile2);

        List<String> diffList = Differ.getDiff(jsonFile1Data, jsonFile2Data);
        String actual = Differ.diffsListToString(diffList);
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
    void diffsListToString_YamlTest() throws IOException {
        Map<String, String> yamlFile1Data = Parser.getDataFromFile(yamlFile1);
        Map<String, String> yamlFile2Data = Parser.getDataFromFile(yamlFile2);

        List<String> diffList = Differ.getDiff(yamlFile1Data, yamlFile2Data);
        String actual = Differ.diffsListToString(diffList);
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
}
