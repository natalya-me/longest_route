package org.natalya_me;

import org.natalya_me.algorithm.Graph;
import org.natalya_me.algorithm.LongestRouteSearch;
import org.natalya_me.util.FileReader;
import org.natalya_me.util.ImmutablePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String DEFAULT_OUTPUT_FILE_NAME = "output.txt";

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Не задан путь до входного файла");
        }
        String inputFilePath = args[0];

        Graph<String> graph = createGraph(inputFilePath);
        List<ImmutablePair<String, String>> longestRoute = LongestRouteSearch.findLongestRoute(graph);

        String outputFilePath = args.length > 1 ? args[1] : DEFAULT_OUTPUT_FILE_NAME;
        File file = new File(outputFilePath);
        if (file.isDirectory()) {
            file = new File(file, DEFAULT_OUTPUT_FILE_NAME);
        }
        String resultData = longestRoute.stream()
                .map(ImmutablePair::getValue)
                .collect(Collectors.joining(" -> "));
        writeResultToFile(file, resultData);
    }

    // Создает граф по данным из файла со строками формата:
    // <идентификатор здания>;<адрес здания>;<идентификатор следующего здания маршрута>
    private static Graph<String> createGraph(String inputFilePath) {
        List<String[]> input = FileReader.readCsvDataFromFile(inputFilePath);
        Graph<String> graph = new Graph<>();
        for (String[] dataLine: input) {
            try {
                graph.addOrUpdateNode(dataLine[0], dataLine[1]);
                if (dataLine.length > 2) {
                    graph.addArc(dataLine[0], dataLine[2]);
                }
            } catch (IndexOutOfBoundsException ex) {
                throw new IllegalArgumentException("Неверный формат данных входного файла");
            }
        }
        return graph;
    }

    private static void writeResultToFile(File file, String data) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}