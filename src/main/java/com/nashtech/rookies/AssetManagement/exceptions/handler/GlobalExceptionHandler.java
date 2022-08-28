package com.nashtech.rookies.AssetManagement.exceptions.handler;

import com.nashtech.rookies.AssetManagement.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ErrorRespond> handleResouceNotFoundException(ResourceNotFoundException ex){
        int statusCode = HttpStatus.NOT_FOUND.value();
        String message = ex.getMessage();
        ErrorRespond errorRespond = new ErrorRespond(statusCode, message);

        return new ResponseEntity<>(errorRespond, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected  ResponseEntity<ErrorRespond> handleAccessDeniedException(AccessDeniedException ex){
        int statusCode = HttpStatus.FORBIDDEN.value();
        String message = "You don't have enough permission to access this api";

        ErrorRespond errorRespond = new ErrorRespond(statusCode, message);
        return new ResponseEntity<>(errorRespond, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request)
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });
        ErrorRespond error = new ErrorRespond(400, "Validation Error", errors);
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolateException.class})
    protected ResponseEntity<?> handleConstraintViolateException(ConstraintViolateException ex){
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String errorMessage = ex.getMessage();
        ErrorRespond errorRespond = new ErrorRespond(statusCode, errorMessage);

        return new ResponseEntity<>(errorRespond, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApiDeniedException.class})
    protected ResponseEntity<?> handleApiDeniedException(ApiDeniedException ex){
        int statusCode = HttpStatus.METHOD_NOT_ALLOWED.value();
        String errorMessage = ex.getMessage();

        ErrorRespond errorRespond = new ErrorRespond(statusCode, errorMessage);
        return new ResponseEntity<>(errorRespond, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ResourceAlreadyExistException.class})
    protected ResponseEntity<?> handleResourceAlreadyExistException(ResourceAlreadyExistException ex){
        int statusCode = HttpStatus.CONFLICT.value();
        String errorMessage = ex.getMessage();

        ErrorRespond errorRespond = new ErrorRespond(statusCode, errorMessage);

        return new ResponseEntity<>(errorRespond, HttpStatus.CONFLICT);
    }


    @ExceptionHandler({NoSuchElementException.class})
    protected ResponseEntity<?> handleOptionalException(NoSuchElementException ex){
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String errorMessage = "Please make sure your value isnt empty before call get() method";
        ErrorRespond errorRespond = new ErrorRespond(statusCode, errorMessage);

        return new ResponseEntity<>(errorRespond, HttpStatus.BAD_REQUEST);

    }
}
