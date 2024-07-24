package com.epam.xm.service;

import static com.epam.xm.support.Constant.ORIGINAL_FILE_NAME_IS_NULL_ERROR_MESSAGE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import com.epam.xm.exception.FileUploadingException;

@Service
@Slf4j
public class CryptoManagementService {

    @Value("${crypto.data.path}")
    private String cryptoDataFolderPath;

    public void upload(MultipartFile file) {
        try {
            String originalFileName = getOriginalFileName(file);
            Path copyLocation = Paths.get(cryptoDataFolderPath + File.separator + StringUtils.cleanPath(originalFileName));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileUploadingException(file.getOriginalFilename());
        }
    }

    private String getOriginalFileName(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new IOException(ORIGINAL_FILE_NAME_IS_NULL_ERROR_MESSAGE);
        }
        return file.getOriginalFilename();
    }
}
