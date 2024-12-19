import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class DataManager {
    public static void main(String[] args) {
        loadData("input.txt");
        long start = System.nanoTime();
        registerDataProcessor(new RankSeparationProcessor());
        processData();
        registerDataProcessor(new ValuesFilterProcessor<>(Clone::getRank, List.of("clone commando")));
        processData();
        threadPool.shutdown();
        long finish = System.nanoTime();
        saveData("output.txt");
        System.out.println((finish - start) / 1000000);
    }

    private static List<Clone> data = new ArrayList<>();
    private static ProcessorInterface dataProcessor;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);


    public static void registerDataProcessor(Object processor) {
        if (processor.getClass().isAnnotationPresent(DataProcessor.class) && processor instanceof ProcessorInterface) {
            dataProcessor = (ProcessorInterface) processor;
        }
    }

    public static void loadData(String source) {
        Path path = Paths.get(source);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] columns = line.split(", ");
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = columns[i].substring(columns[i].indexOf(":") + 2);
                }
                data.add(new Clone(columns[0], columns[1], columns[2], Integer.parseInt(columns[3]), Integer.parseInt(columns[4]), null, -1.0));
            }
        } catch (IOException e) {
            System.out.println("Data wasn't loaded from file");
            e.printStackTrace();
        }
    }

    public static void processData() {
        if (dataProcessor == null) {
            System.out.println("There is no dataProcessor to process data, register it");
            return;
        }
        if (data.size() == 0) {
            System.out.println("There is no data");
            return;
        }
        int nThreads = (data.size() >= 1000) ?  1 : 1;
        int subListSize = (int)Math.ceil((double)data.size() / nThreads);
        List<Future<List<Clone>>> results = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            List<Clone> subData;
            if (i * subListSize + subListSize <= data.size()) subData = data.subList(i * subListSize, i * subListSize + subListSize);
            else subData = data.subList(i * subListSize, data.size());
            results.add(threadPool.submit(new Callable<List<Clone>>() {
                @Override
                public List<Clone> call() {
                    System.out.println(Thread.currentThread().getName() + " " + subData.size());
                    return dataProcessor.process(subData);
                }
            }));
        }
        List<Clone> processedData = new ArrayList<>();
        for (Future<List<Clone>> future : results) {
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
            List<String> lines = new ArrayList<>();
            for (Clone clone : data) {
                lines.add(clone.toString());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Data wasn't saved to file");
            e.printStackTrace();
        }
    }
}
