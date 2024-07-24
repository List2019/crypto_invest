package com.epam.xm.service;

import static com.epam.xm.support.Constant.CSV;
import static com.epam.xm.support.Constant.HEADERS;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.epam.xm.exception.CSVReadException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CSVService {

    @Value("${crypto.data.path}")
    private String cryptoDataFolderPath;

    public List<List<CSVRecord>> readAllCryptoDataFiles() {
        List<List<CSVRecord>> result = new ArrayList<>();
        File[] listOfFiles = getListOfFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(CSV)) {
                List<CSVRecord> csvRecords = readCsvFile(file.getAbsolutePath());
                result.add(csvRecords);
            }
        }

        return result;
    }

    public List<CSVRecord> readCryptoDataFile(String cryptoName) {
        List<CSVRecord> records = new ArrayList<>();
        File[] listOfFiles = getListOfFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(CSV) && file.getName().contains(cryptoName)) {
                records = readCsvFile(file.getAbsolutePath());
            }
        }

        return records;
    }

    public List<String> getAllCryptoDataFileNames() {
        List<String> cryptoDataFileNames = new ArrayList<>();
        File[] listOfFiles = getListOfFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(CSV)) {
                cryptoDataFileNames.add(file.getName());
            }
        }

        return cryptoDataFileNames;
    }

    private File[] getListOfFiles() {
        File folder = new File(cryptoDataFolderPath);
        return folder.listFiles();
    }

    public List<CSVRecord> readCsvFile(String fileName) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);

            List<CSVRecord> recordList = new ArrayList<>();
            records.forEach(recordList::add);
            return recordList;
        } catch (Exception e) {
            throw new CSVReadException();
        }
    }
}
