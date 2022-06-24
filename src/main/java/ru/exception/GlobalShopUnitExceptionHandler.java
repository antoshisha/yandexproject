package ru.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalShopUnitExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Error> handleException(ShopUnitVerifyException exception) {
        Error errorMessage = new Error();
        errorMessage.setCode(400);
        errorMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleException(DateTimeParseException exception) {
        Error errorMessage = new Error();
        errorMessage.setCode(400);
        errorMessage.setMessage("Validation failed!");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleException(ShopUnitNotFoundException exception) {
        Error errorMessage = new Error();
        errorMessage.setCode(404);
        errorMessage.setMessage(exception.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
