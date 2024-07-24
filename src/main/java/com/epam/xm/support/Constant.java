package com.epam.xm.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String BASE_MAPPING_URL = "/api/v1";
    public static final String UTC = "UTC";
    public static final String CSV = ".csv";
    public static final String[] HEADERS = {"timestamp", "symbol", "price"};
    public static final double DEFAULT_PRICE = Double.parseDouble("0");

    public static final String FAILED_TO_READ_CSV_FILE_ERROR_MESSAGE = "Failed to read crypto data from file";
    public static final String UNSUPPORTED_CRYPTO_ERROR_MESSAGE = "Crypto %s is not supported yet, please consider to upload it";
    public static final String NEW_CRYPTO_UPLOAD_ERROR_MESSAGE = "Could not upload file %s. Please try again!";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Oops, something went wrong!";
    public static final String ORIGINAL_FILE_NAME_IS_NULL_ERROR_MESSAGE = "Original file name is null!";
    public static final String DATA_NOT_FOUND_ERROR_MESSAGE = "Data was not found for the %s";

}
