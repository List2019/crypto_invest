package com.epam.xm.exception;

import static com.epam.xm.support.Constant.FAILED_TO_READ_CSV_FILE_ERROR_MESSAGE;

public class CSVReadException extends RuntimeException {
    public CSVReadException() {
        super(String.format(FAILED_TO_READ_CSV_FILE_ERROR_MESSAGE));
    }
}
