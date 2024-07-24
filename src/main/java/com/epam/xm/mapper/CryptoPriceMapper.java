package com.epam.xm.mapper;

import static com.epam.xm.support.Constant.HEADERS;

import java.time.Instant;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.epam.xm.model.CryptoPrice;

@Service
public class CryptoPriceMapper {

    public List<CryptoPrice> map(List<CSVRecord> csvRecords) {
        return csvRecords.stream()
                .map(this::map)
                .toList();
    }

    public CryptoPrice map(CSVRecord csvRecord) {
        long timestampLong = Long.parseLong(csvRecord.get(HEADERS[0]));
        Instant timestamp = Instant.ofEpochMilli(timestampLong);
        String cryptoName = csvRecord.get(HEADERS[1]);
        Double price = Double.parseDouble(csvRecord.get(HEADERS[2]));

        return new CryptoPrice(timestamp, cryptoName, price);
    }
}
