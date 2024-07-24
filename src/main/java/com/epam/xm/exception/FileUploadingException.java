package com.epam.xm.exception;

import static com.epam.xm.support.Constant.NEW_CRYPTO_UPLOAD_ERROR_MESSAGE;

public class FileUploadingException extends RuntimeException {
    public FileUploadingException(String fileName) {
        super(String.format(NEW_CRYPTO_UPLOAD_ERROR_MESSAGE, fileName));
    }
}
