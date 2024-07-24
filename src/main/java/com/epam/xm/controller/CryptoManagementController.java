package com.epam.xm.controller;

import static com.epam.xm.support.Constant.BASE_MAPPING_URL;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.epam.xm.service.CryptoManagementService;

@Controller
@RequestMapping(BASE_MAPPING_URL)
@RequiredArgsConstructor
public class CryptoManagementController implements CryptoManagementApi {

    private final CryptoManagementService cryptoManagementService;

    @Override
    public ResponseEntity<Void> addNewCrypto(MultipartFile file) {
        cryptoManagementService.upload(file);
        return ResponseEntity.ok().build();
    }
}
