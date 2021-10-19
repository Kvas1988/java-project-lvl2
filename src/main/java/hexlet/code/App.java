package hexlet.code;

import picocli.CommandLine;

@CommandLine.Command(name = "gendiff", version = "Differ 1.0", mixinStandardHelpOptions = true)
public class App implements Runnable {

    // @CommandLine.Option(names = {"-u", "--upper"}, description = "Upper Case")
    // boolean isUpperCase = false;
    //
    // @CommandLine.Parameters(paramLabel = "<word>", defaultValue = "Hello, picocli",
    //         description = "Word to be printed.")
    // String text = "Hello picocli"; // defaultValue in @Parameters will be used if no given in args

    @Override
    public void run() {

        String diff = Differ.generate("hello", "world");
        System.out.println(diff);
    }




    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}