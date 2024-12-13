import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataManager {
    public static void main(String[] args) {
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

    public static void loadData(String source) {
        Path path = Paths.get(source);
        try {
            data = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Data wasn't loaded from file");
            e.printStackTrace();
        }
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
        int nThreads = 3;
        int subListSize = (int)Math.ceil((double)data.size() / nThreads);
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        ArrayList<Future<List<String>>> results = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            List<String> subData;
            if (i * subListSize + subListSize <= data.size()) subData = data.subList(i * subListSize, i * subListSize + subListSize);
            else subData = data.subList(i * subListSize, data.size());
            results.add(threadPool.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() {
                    return dataProcessor.process(subData);
                }
            }));
        }
        threadPool.shutdown();
        List<String> processedData = new ArrayList<>();
        for (Future<List<String>> future : results) {
            try {
                processedData.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        data = processedData;
    }

    public static void saveData(String destination) {
        Path path = Paths.get(destination);
        try {
            Files.write(path, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Data wasn't saved to file");
            e.printStackTrace();
        }
    }
}
