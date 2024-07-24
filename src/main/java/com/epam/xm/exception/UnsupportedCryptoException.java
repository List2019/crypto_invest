package com.epam.xm.exception;

import static com.epam.xm.support.Constant.UNSUPPORTED_CRYPTO_ERROR_MESSAGE;

public class UnsupportedCryptoException extends RuntimeException {
    public UnsupportedCryptoException(String fileName) {
        super(String.format(UNSUPPORTED_CRYPTO_ERROR_MESSAGE, fileName));
    }
}
