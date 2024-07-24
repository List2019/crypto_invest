package com.epam.xm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.epam.xm.exception.CSVReadException;

@ExtendWith(MockitoExtension.class)
public class CSVServiceTest {

    @Spy
    private CSVService csvService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(csvService, "cryptoDataFolderPath", "src/test/resources/crypto_data");
    }

    @Test
    public void testReadAllCryptoDataFiles() {
        List<List<CSVRecord>> result = csvService.readAllCryptoDataFiles();

        assertEquals(5, result.size());
        assertEquals(100, result.getFirst().size());
    }

    @Test
    public void testReadCryptoDataFile() {
        List<CSVRecord> result = csvService.readCryptoDataFile("DOGE");

        assertEquals(90, result.size());
    }

    @Test
    public void testGetAllCryptoDataFileNames() {
        List<String> result = csvService.getAllCryptoDataFileNames();

        assertEquals(5, result.size());
        assertTrue(result.contains("XRP_values.csv"));
    }

    @Test
    public void testReadCsvFileException() {
        assertThrows(CSVReadException.class, () -> csvService.readCsvFile("nonexistent.csv"));
    }
}
