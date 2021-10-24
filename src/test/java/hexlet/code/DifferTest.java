package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DifferTest {

    private String filename1;
    private String filename2;
    private String notExistingFilepath;
    private String notJsonFilepath;
    private String emptyJsonFilepath;
    private String jsonStringFile1;

    private File file1;
    private File file2;
    private File notExistingFile;
    private File notJsonFile;
    private File emptyJsonFile;

    @BeforeEach
    void setup() {
        filename1 = "src/test/resources/file1.json";
        filename2 = "src/test/resources/file2.json";
        notExistingFilepath = "src/test/resources/I_DONT_EXIST.json";
        notJsonFilepath = "src/test/resources/notJson.json";
        emptyJsonFilepath = "src/test/resources/empty.json";

        file1 = Differ.getFileObj(filename1);
        file2 = Differ.getFileObj(filename2);
        notExistingFile = Differ.getFileObj(notExistingFilepath);
        notJsonFile = Differ.getFileObj(notJsonFilepath);
        emptyJsonFile = Differ.getFileObj(emptyJsonFilepath);


        jsonStringFile1 = "{ \"host\" : \"hexlet.io\", \"timeout\" : 50, \"proxy\" : \"123.234.53.22\", " +
                "\"follow\": false }";


    }

    // region getFileObj Test
    @Test
    void getFileObj_RelativePathTest() {
        String absFilepath = System.getProperty("user.id") + "/" + filename1;
        File expected = new File(absFilepath);
        assertEquals(expected, file1);
    }

    @Test
    void getFileObj_FileExistsTest() {
        assertTrue(!notExistingFile.exists());

        File file1 = Differ.getFileObj(filename1);
        assertTrue(file1.exists());
    }

    // endregion

    // region getDataFromJsonFile
    @Test
    void getDataFromJsonFile_File1Test() throws IOException {
        JsonNode actual = Differ.getDataFromJsonFile(file1);
        JsonNode expected = new ObjectMapper().readTree(jsonStringFile1);
        assertEquals(expected, actual);

        JsonNode file2Node = Differ.getDataFromJsonFile(file2);
        assertNotEquals(actual, file2Node);
    }

    @Test
    void getDataFromJsonFile_ExceptionThrownTest() {
        assertThrows(IOException.class, () -> {
            Differ.getDataFromJsonFile(notExistingFile);
        });

        assertThrows(IOException.class, () -> {
            Differ.getDataFromJsonFile(notJsonFile);
        });
    }

    // endregion

    // region getFields
    // TODO: WE NEED MORE TESTS
    @Test
    void getFields_Test() throws IOException {
        JsonNode file1Node = Differ.getDataFromJsonFile(file1);
        Set<String> actual = Differ.getFields(file1Node);

        Set<String> expected = Set.of("host", "timeout", "proxy", "follow");
        assertEquals(expected, actual, "Wrong set of strings");
    }

    @Test
    void getFields_EmptyJsonTest() throws IOException {
        JsonNode emptyNode = Differ.getDataFromJsonFile(emptyJsonFile);
        Set<String> actual = Differ.getFields(emptyNode);

        Set<String> expected = new HashSet<>();
        assertEquals(expected, actual, "Empty JsonFile has not empty JsonNode. WTF???");
    }

    // endregion

    // region getDiff Test
    // TODO: WE NEED MORE TESTS
    @Test
    void getDiff_Test() throws IOException {
        JsonNode file1Node = Differ.getDataFromJsonFile(file1);
        JsonNode file2Node = Differ.getDataFromJsonFile(file2);

        List<String> actual = Differ.getDiff(file1Node, file2Node);
        List<String> expected = List.of("  - follow: false",
                "    host: hexlet.io",
                "  - proxy: 123.234.53.22",
                "  - timeout: 50",
                "  + timeout: 20",
                "  + verbose: true"
                );
        assertEquals(expected, actual);
    }

    // endregion

    // region diffsListToString Test
    @Test
    void diffsListToString_Test() throws IOException {
        JsonNode file1Node = Differ.getDataFromJsonFile(file1);
        JsonNode file2Node = Differ.getDataFromJsonFile(file2);

        List<String> diffList = Differ.getDiff(file1Node, file2Node);
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

    // endregions
}
