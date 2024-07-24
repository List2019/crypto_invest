package com.epam.xm.service;

import static com.epam.xm.TestConstant.BTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.xm.model.CryptoPrice;
import com.epam.xm.model.CryptoStatisticResponseDto;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTest {

    @InjectMocks
    private StatisticService statisticService;

    @Test
    public void testCalculateStatistic() {
        List<CryptoPrice> cryptoPrices = Arrays.asList(
                new CryptoPrice(Instant.now(), BTC, 100.0),
                new CryptoPrice(Instant.now().plusSeconds(10), BTC, 200.0),
                new CryptoPrice(Instant.now().plusSeconds(20), BTC, 150.0)
        );

        CryptoStatisticResponseDto result = statisticService.calculateStatistic(BTC, cryptoPrices);

        assertEquals(BTC, result.getCryptoName());
        assertEquals(100.0, result.getOldestPrice());
        assertEquals(150.0, result.getNewestPrice());
        assertEquals(100.0, result.getMinPrice());
        assertEquals(200.0, result.getMaxPrice());
    }

    @Test
    public void testGetNormalizedIndex() {
        List<CryptoPrice> cryptoPrices = Arrays.asList(
                new CryptoPrice(Instant.now(), BTC, 100.0),
                new CryptoPrice(Instant.now().plusSeconds(10), BTC, 200.0),
                new CryptoPrice(Instant.now().plusSeconds(20), BTC, 150.0)
        );

        Double result = statisticService.getNormalizedIndex(cryptoPrices);

        assertEquals(1.0, result);
    }
}
