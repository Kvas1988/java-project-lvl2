package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "gendiff", version = "Differ 1.0", mixinStandardHelpOptions = true)
public class App implements Callable<String> {

    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]")
    String format = "format";

    @Parameters(paramLabel = "filepath1", defaultValue = "Hello, picocli",
            description = "Path to first file")
    String filePath1 = "Hello picocli"; // defaultValue in @Parameters will be used if no given in args

    @Parameters(paramLabel = "filepath2", defaultValue = "Hello, picocli",
            description = "Path to second file")
    String filePath2 = "Hello picocli";

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