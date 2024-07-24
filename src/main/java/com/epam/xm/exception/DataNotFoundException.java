package com.epam.xm.exception;

import static com.epam.xm.support.Constant.DATA_NOT_FOUND_ERROR_MESSAGE;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String date) {
        super(String.format(DATA_NOT_FOUND_ERROR_MESSAGE, date));
    }
}
