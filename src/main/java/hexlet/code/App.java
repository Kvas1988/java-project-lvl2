package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "gendiff", version = "Differ 1.0", mixinStandardHelpOptions = true)
public class App implements Callable<String> {

    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]")
    private String format = "format";

    @Parameters(paramLabel = "filepath1", description = "Path to first file")
    private String filePath1 = "Hello picocli"; // defaultValue in @Parameters will be used if no given in args

    @Parameters(paramLabel = "filepath2", description = "Path to second file")
    private String filePath2 = "file2.json";

    /**
     * Picocli uses this function to implement CLI.
     * @return Formatted string with diff of two json files given
     * @throws Exception may throw IOException when parsing given files.
     */
    @Override
    public String call() throws Exception {
        String diff = Differ.generate(filePath1, filePath2);
        System.out.println(diff);
        return diff;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
