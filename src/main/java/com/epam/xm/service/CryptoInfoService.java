package com.epam.xm.service;

import static com.epam.xm.support.Constant.DEFAULT_PRICE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.epam.xm.exception.DataNotFoundException;
import com.epam.xm.exception.UnsupportedCryptoException;
import com.epam.xm.mapper.CryptoPriceMapper;
import com.epam.xm.model.CryptoPrice;
import com.epam.xm.model.CryptoStatisticResponseDto;

@Service
@RequiredArgsConstructor
public class CryptoInfoService {
    private final CSVService csvService;
    private final StatisticService statisticService;
    private final CryptoPriceMapper cryptoPriceMapper;

    public List<CryptoStatisticResponseDto> calculateStatisticForAllCryptos(LocalDate startDate, LocalDate endDate) {
        List<CryptoStatisticResponseDto> cryptoStatistics = new ArrayList<>();
        List<List<CryptoPrice>> allCryptoPrices = getAllCryptoPrices();

        for (List<CryptoPrice> cryptoPrice : allCryptoPrices) {
            List<CryptoPrice> filteredCryptoPrices = cryptoPrice.stream()
                    .filter(record -> {
                        LocalDate recordDate = record.getLocalDate();
                        return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                    })
                    .toList();

            CryptoStatisticResponseDto cryptoStatistic = statisticService.calculateStatistic(cryptoPrice.getFirst().cryptoName(), filteredCryptoPrices);
            cryptoStatistics.add(cryptoStatistic);
        }

        if (cryptoStatistics.stream().allMatch(this::isStatisticsEmpty)) {
            String date = String.join(" - ", startDate.toString(), endDate.toString());
            throw new DataNotFoundException(date);
        }

        return cryptoStatistics;
    }

    public CryptoStatisticResponseDto getStatisticForSpecificCrypto(String cryptoName) {
        validateCryptoName(cryptoName);
        List<CryptoPrice> cryptoPrices = csvService.readCryptoDataFile(cryptoName)
                .stream()
                .map(cryptoPriceMapper::map)
                .toList();

        return statisticService.calculateStatistic(cryptoPrices.getFirst().cryptoName(), cryptoPrices);
    }

    public List<String> getNormalizedRangeSortedCryptos() {
        Map<String, Double> cryptoWithNormalizedIndex = new HashMap<>();
        List<List<CryptoPrice>> allCryptoPrices = getAllCryptoPrices();

        for (List<CryptoPrice> cryptoPrices : allCryptoPrices) {
            Double normalizedIndex = statisticService.getNormalizedIndex(cryptoPrices);
            cryptoWithNormalizedIndex.put(cryptoPrices.getFirst().cryptoName(), normalizedIndex);
        }

        return cryptoWithNormalizedIndex.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<String> getAllSupportedCryptoNames() {
        List<String> registeredCryptoNames = new ArrayList<>();

        for (String cryptoDataFileName : csvService.getAllCryptoDataFileNames()) {
            String[] parts = cryptoDataFileName.split("_");
            registeredCryptoNames.add(parts[0]);
        }
        return registeredCryptoNames;
    }

    public String getHighestNormalizedCryptoName(LocalDate date) {
        List<List<CryptoPrice>> allCryptoPrices = getAllCryptoPrices();
        Map<String, Double> cryptoWithNormalizedIndex = new HashMap<>();

        for (List<CryptoPrice> cryptoPrices : allCryptoPrices) {
            List<CryptoPrice> filteredCryptoPrices = cryptoPrices.stream()
                    .filter(cryptoPrice -> {
                        LocalDate cryptoPriceDate = cryptoPrice.getLocalDate();
                        return date.equals(cryptoPriceDate);
                    })
                    .toList();
            Double normalizedIndex = statisticService.getNormalizedIndex(filteredCryptoPrices);
            cryptoWithNormalizedIndex.put(cryptoPrices.getFirst().cryptoName(), normalizedIndex);
        }

        if (cryptoWithNormalizedIndex.values().stream().allMatch(normalizedIndex -> normalizedIndex.isNaN())) {
            throw new DataNotFoundException(date.toString());
        }

        return Collections.max(cryptoWithNormalizedIndex.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private List<List<CryptoPrice>> getAllCryptoPrices() {
        List<List<CryptoPrice>> allCryptoPrices = new ArrayList<>();
        List<List<CSVRecord>> csvRecords = csvService.readAllCryptoDataFiles();

        for (List<CSVRecord> records : csvRecords) {
            List<CryptoPrice> cryptoPrices = cryptoPriceMapper.map(records);
            allCryptoPrices.add(cryptoPrices);
        }
        return allCryptoPrices;
    }

    private void validateCryptoName(String cryptoName) {
        if (!getAllSupportedCryptoNames().contains(cryptoName)) {
            throw new UnsupportedCryptoException(cryptoName);
        }
    }

    private boolean isStatisticsEmpty(CryptoStatisticResponseDto cryptoStatistic) {
        boolean isMaxPriceEmpty = cryptoStatistic.getMaxPrice().equals(DEFAULT_PRICE);
        boolean isMinPriceEmpty = cryptoStatistic.getMinPrice().equals(DEFAULT_PRICE);
        boolean isNewestPriceEmpty = cryptoStatistic.getNewestPrice().equals(DEFAULT_PRICE);
        boolean isOldestPriceEmpty = cryptoStatistic.getOldestPrice().equals(DEFAULT_PRICE);
        return isMaxPriceEmpty && isMinPriceEmpty && isNewestPriceEmpty && isOldestPriceEmpty;
    }
}
