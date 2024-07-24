package com.epam.xm.service;

import static com.epam.xm.support.Constant.DEFAULT_PRICE;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.epam.xm.model.CryptoPrice;
import com.epam.xm.model.CryptoStatisticResponseDto;

@Service
public class StatisticService {

    public CryptoStatisticResponseDto calculateStatistic(String cryptoName, List<CryptoPrice> cryptoPrices) {
        var cryptoStatistic = new CryptoStatisticResponseDto();
        cryptoStatistic.setOldestPrice(getOldestPrice(cryptoPrices));
        cryptoStatistic.setNewestPrice(getNewestPrice(cryptoPrices));
        cryptoStatistic.setMinPrice(getMinPrice(cryptoPrices));
        cryptoStatistic.setMaxPrice(getMaxPrice(cryptoPrices));
        cryptoStatistic.setCryptoName(cryptoName);
        return cryptoStatistic;
    }

    public Double getNormalizedIndex(List<CryptoPrice> cryptoPrices) {
        Double maxPrice = getMaxPrice(cryptoPrices);
        Double minPrice = getMinPrice(cryptoPrices);

        return (maxPrice - minPrice) / minPrice;
    }

    private Double getOldestPrice(List<CryptoPrice> cryptoPrices) {
        return cryptoPrices.stream()
                .min(Comparator.comparing(CryptoPrice::timestamp))
                .map(CryptoPrice::price)
                .orElse(DEFAULT_PRICE);
    }

    private Double getNewestPrice(List<CryptoPrice> cryptoPrices) {
        return cryptoPrices.stream()
                .max(Comparator.comparing(CryptoPrice::timestamp))
                .map(CryptoPrice::price)
                .orElse(DEFAULT_PRICE);
    }

    private Double getMinPrice(List<CryptoPrice> cryptoPrices) {
        return cryptoPrices.stream()
                .min(Comparator.comparing(CryptoPrice::price))
                .map(CryptoPrice::price)
                .orElse(DEFAULT_PRICE);
    }

    private Double getMaxPrice(List<CryptoPrice> cryptoPrices) {
        return cryptoPrices.stream()
                .max(Comparator.comparing(CryptoPrice::price))
                .map(CryptoPrice::price)
                .orElse(DEFAULT_PRICE);
    }
}
