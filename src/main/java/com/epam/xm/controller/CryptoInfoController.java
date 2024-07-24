package com.epam.xm.controller;

import static com.epam.xm.support.Constant.BASE_MAPPING_URL;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import com.epam.xm.model.AllCryptosStatisticResponseDto;
import com.epam.xm.model.CryptoNamesResponseDto;
import com.epam.xm.model.CryptoStatisticResponseDto;
import com.epam.xm.model.HighestNormalizedForSpecificDayCryptoResponseDto;
import com.epam.xm.model.NormalizedRangeSortedCryptosResponseDto;
import com.epam.xm.service.CryptoInfoService;

@Controller
@RequestMapping(BASE_MAPPING_URL)
@RequiredArgsConstructor
public class CryptoInfoController implements CryptoInformationApi {

    private final CryptoInfoService cryptoInfoService;

    @Override
    public ResponseEntity<AllCryptosStatisticResponseDto> getStatisticForAllCryptos(LocalDate startDate, LocalDate endDate) {
        List<CryptoStatisticResponseDto> cryptoStatistics = cryptoInfoService.calculateStatisticForAllCryptos(startDate, endDate);
        return ResponseEntity.ok(
                new AllCryptosStatisticResponseDto()
                        .cryptoStatistic(cryptoStatistics)
        );
    }

    @Override
    public ResponseEntity<NormalizedRangeSortedCryptosResponseDto> getNormalizedRangeSortedCryptos() {
        List<String> normalizedRangeSortedCryptos = cryptoInfoService.getNormalizedRangeSortedCryptos();
        return ResponseEntity.ok(
                new NormalizedRangeSortedCryptosResponseDto()
                        .cryptos(normalizedRangeSortedCryptos)
        );
    }

    @Override
    public ResponseEntity<CryptoNamesResponseDto> getSupportedCryptoNames() {
        List<String> allRegisteredCryptoNames = cryptoInfoService.getAllSupportedCryptoNames();
        return ResponseEntity.ok(new CryptoNamesResponseDto().cryptos(allRegisteredCryptoNames));
    }

    @Override
    public ResponseEntity<CryptoStatisticResponseDto> getStatisticForSpecificCrypto(String cryptoName) {
        CryptoStatisticResponseDto statisticForSpecificCrypto = cryptoInfoService.getStatisticForSpecificCrypto(cryptoName);
        return ResponseEntity.ok(statisticForSpecificCrypto);
    }

    @Override
    public ResponseEntity<HighestNormalizedForSpecificDayCryptoResponseDto> getHighestNormalizedForSpecificDayCrypto(LocalDate date) {
        String highestNormalizedCryptoName = cryptoInfoService.getHighestNormalizedCryptoName(date);
        return ResponseEntity.ok(
                new HighestNormalizedForSpecificDayCryptoResponseDto()
                        .cryptoName(highestNormalizedCryptoName)
        );
    }
}
