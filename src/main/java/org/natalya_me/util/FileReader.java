package org.natalya_me.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *   Утилитный класс для чтения данных из текстового файла в формате CSV с разделителем ';'.
 */
public class FileReader {

    private static final String DELIMITER = ";";

    private FileReader() {
        throw new UnsupportedOperationException(String.format("Инстанцирование класса %s запрещено.", FileReader.class.getName()));
    }

    /**
     * Читает данные из файла, разделяя значения в строках по разделителю.
     *
     * @param filePath путь к текстовому файлу
     * @return         структура List<String[]> с извлеченными данными
     */
    public static List<String[]> readCsvDataFromFile(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Путь к файлу не может быть null.");
        }

        File file = new File(filePath);
        if (file.isFile() && file.canRead()) {
            List<String[]> records = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    records.add(line.split(DELIMITER));
                }
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(String.format("Файл %s не найден.", filePath));
            }
            return records;
        } else {
            throw new IllegalArgumentException(String.format("%s не является корректным путем к файлу, или отсутствует право на чтение.", filePath));
        }
    }
}
