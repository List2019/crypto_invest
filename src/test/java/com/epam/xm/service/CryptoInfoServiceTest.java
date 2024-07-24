package com.epam.xm.service;

import static com.epam.xm.TestConstant.BTC;
import static com.epam.xm.TestConstant.ETH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.xm.exception.DataNotFoundException;
import com.epam.xm.exception.UnsupportedCryptoException;
import com.epam.xm.mapper.CryptoPriceMapper;
import com.epam.xm.model.CryptoPrice;
import com.epam.xm.model.CryptoStatisticResponseDto;


@ExtendWith(MockitoExtension.class)
public class CryptoInfoServiceTest {

    @InjectMocks
    private CryptoInfoService cryptoInfoService;

    @Mock
    private CSVService csvService;

    @Mock
    private StatisticService statisticService;

    @Mock
    private CSVRecord csvRecord;

    @Mock
    private CryptoPriceMapper cryptoPriceMapper;

    @Test
    public void testCalculateStatisticForAllCryptos() {
        var cryptoPrice = new CryptoPrice(Instant.now(), BTC, 100.0);
        var cryptoStatistic = new CryptoStatisticResponseDto();
        cryptoStatistic.setMaxPrice(Double.MAX_VALUE);
        cryptoStatistic.setMinPrice(Double.MIN_VALUE);
        cryptoStatistic.setNewestPrice(Double.MIN_VALUE);
        cryptoStatistic.setOldestPrice(Double.MIN_VALUE);

        List<CSVRecord> records = List.of(csvRecord);
        when(csvService.readAllCryptoDataFiles()).thenReturn(List.of(records));
        when(cryptoPriceMapper.map(records)).thenReturn(List.of(cryptoPrice));
        when(statisticService.calculateStatistic(BTC, List.of(cryptoPrice))).thenReturn(cryptoStatistic);

        List<CryptoStatisticResponseDto> result = cryptoInfoService.calculateStatisticForAllCryptos(LocalDate.now().minusDays(2), LocalDate.now());

        assertEquals(1, result.size());
    }

    @Test
    public void testCalculateStatisticForAllCryptosThrowsException() {
        var cryptoPrice = new CryptoPrice(Instant.now(), BTC, 100.0);
        List<CSVRecord> records = List.of(csvRecord);
        when(csvService.readAllCryptoDataFiles()).thenReturn(List.of(records));
        when(cryptoPriceMapper.map(records)).thenReturn(List.of(cryptoPrice));
        when(statisticService.calculateStatistic(eq(BTC), anyList())).thenCallRealMethod();

        LocalDate wrongStartDate = LocalDate.now().minusDays(10);
        LocalDate wrongEndDate = LocalDate.now().minusDays(5);

        assertThrows(DataNotFoundException.class, () -> cryptoInfoService.calculateStatisticForAllCryptos(wrongStartDate, wrongEndDate));
    }

    @Test
    public void testGetHighestNormalizedCryptoName() {
        //  Wednesday, July 24, 2024 10:03:33 PM into timestamp
        long timestampLong = Long.parseLong("1721858613000");
        Instant timestamp = Instant.ofEpochMilli(timestampLong);

        var cryptoPrice = new CryptoPrice(timestamp, BTC, 100.0);
        List<CSVRecord> records = List.of(csvRecord);
        when(csvService.readAllCryptoDataFiles()).thenReturn(List.of(records));
        when(cryptoPriceMapper.map(records)).thenReturn(List.of(cryptoPrice));
        when(statisticService.getNormalizedIndex(List.of(cryptoPrice))).thenReturn(1.0);

        String result = cryptoInfoService.getHighestNormalizedCryptoName(LocalDate.of(2024, 7, 24));

        assertEquals(BTC, result);
    }

    @Test
    public void testGetHighestNormalizedCryptoNameThrowsException() {
        //  Wednesday, July 24, 2024 10:03:33 PM into timestamp
        long timestampLong = Long.parseLong("1721858613000");
        Instant timestamp = Instant.ofEpochMilli(timestampLong);

        var cryptoPrice = new CryptoPrice(timestamp, BTC, 100.0);
        List<CSVRecord> records = List.of(csvRecord);
        when(csvService.readAllCryptoDataFiles()).thenReturn(List.of(records));
        when(cryptoPriceMapper.map(records)).thenReturn(List.of(cryptoPrice));
        when(statisticService.getNormalizedIndex(anyList())).thenCallRealMethod();
        LocalDate wrongDate = LocalDate.of(2019, 7, 24);

        assertThrows(DataNotFoundException.class, () -> cryptoInfoService.getHighestNormalizedCryptoName(wrongDate));
    }

    @Test
    public void testGetStatisticForSpecificCryptoUnsupported() {
        when(csvService.getAllCryptoDataFileNames()).thenReturn(List.of(ETH));
        assertThrows(UnsupportedCryptoException.class, () -> cryptoInfoService.getStatisticForSpecificCrypto(BTC));
    }
}
