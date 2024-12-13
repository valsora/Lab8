import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataManager {
    public static void main(String[] args) throws IOException {
        registerDataProcessor(new FilterProcessor());
        loadData("input.txt");
        processData();
        saveData("output.txt");
    }

    private static List<String> data;
    private static ProcessorInterface dataProcessor;


    public static void registerDataProcessor(Object processor) {
        if (processor.getClass().isAnnotationPresent(DataProcessor.class) && processor instanceof ProcessorInterface) {
            dataProcessor = (ProcessorInterface) processor;
        }
    }

    public static void loadData(String source) throws IOException {
        Path path = Paths.get(source);
        data = Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public static void processData() {
        if (dataProcessor == null) {
            System.out.println("There is no dataProcessor to process data");
            return;
        }
        if (data == null) {
            System.out.println("There is no data");
            return;
        }
        //threadpool
        data = dataProcessor.process(data);
    }

    public static void saveData(String destination) throws IOException {
        Path path = Paths.get(destination);
        Files.write(path, data, StandardCharsets.UTF_8);
    }
}
