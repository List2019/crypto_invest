package com.epam.xm.config;

import static com.epam.xm.support.Constant.INTERNAL_SERVER_ERROR_MESSAGE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import com.epam.xm.exception.CSVReadException;
import com.epam.xm.exception.DataNotFoundException;
import com.epam.xm.exception.FileUploadingException;
import com.epam.xm.exception.UnsupportedCryptoException;
import com.epam.xm.model.ErrorResponseDto;

@ControllerAdvice
@Slf4j
public class CryptoInvestExceptionHandler {

    @ExceptionHandler({CSVReadException.class, FileUploadingException.class})
    protected ResponseEntity<ErrorResponseDto> handleInternalServerExceptions(Exception e) {
        log.error("Internal server error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto().message(e.getMessage()));
    }

    @ExceptionHandler({UnsupportedCryptoException.class})
    protected ResponseEntity<ErrorResponseDto> handleBadRequestException(Exception e) {
        log.error("Bad request occurred", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto().message(e.getMessage()));
    }

    @ExceptionHandler({DataNotFoundException.class})
    protected ResponseEntity<ErrorResponseDto> handleDataNotFoundException(Exception e) {
        log.error("Data was not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto().message(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponseDto> handleGeneralExceptions(Exception e) {
        log.error("General exceptions occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto().message(INTERNAL_SERVER_ERROR_MESSAGE));
    }
}
