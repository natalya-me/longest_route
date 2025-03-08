package org.natalya_me.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileReader {

    @Test
    void testReadCsvDataFromFileWhenFilePathNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> FileReader.readCsvDataFromFile(null));
    }

    @Test
    void testReadCsvDataFromFileWhenFilePathDoesNotExist() {
        assertThrowsExactly(IllegalArgumentException.class, () -> FileReader.readCsvDataFromFile("src/test/resources/no_such_file.txt"));
    }

    @Test
    void testReadCsvDataFromFileWhenFileExists() {
        List<String[]> result = FileReader.readCsvDataFromFile("src/test/resources/input.txt");
        assertEquals(9, result.size());
        for (String[] arr: result) {
            assertTrue(Arrays.asList(2, 3).contains(arr.length));
        }
    }
}
