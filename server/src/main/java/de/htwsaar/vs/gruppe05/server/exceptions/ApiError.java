package de.htwsaar.vs.gruppe05.server.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * API Error Object
 *
 * @version 20.02.2023
 */
@Data
public class ApiError {

    private static final String DEFAULT_MSG = "Unexpected Error ;(";

    private HttpStatus httpStatus;
    private String message;
    private String description;

    private LocalDateTime timestamp;
    private List<ApiSubErrors> errors;


    public ApiError(HttpStatus httpStatus, String message, Throwable ex) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.description = ex.getMessage();
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public ApiError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = DEFAULT_MSG;
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public ApiError(HttpStatus httpStatus, String message, String description, List<ApiSubErrors> errors) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.description = description;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates Validation Error by FieldError
     *
     * @param fieldError
     */
    public void addValidationSubErrorByFieldError(FieldError fieldError) {
        ApiValidationError validationError = new ApiValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        this.errors.add(validationError);
    }

    /**
     * Creates Validation Error by FieldError-List
     *
     * @param fieldErrors
     */
    public void addValidationSubErrorsByFieldError(List<FieldError> fieldErrors) {
        fieldErrors.forEach(fieldError -> addValidationSubErrorByFieldError(fieldError));
    }

    /**
     * Creates Validation Error by ObjectError
     *
     * @param objectError
     */
    private void addValidationSubErrorByObjectError(ObjectError objectError) {
        ApiValidationError apiValErr = new ApiValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
        this.errors.add(apiValErr);
    }

    /**
     * Creates Validation Error by ObjectError-List
     *
     * @param objectErrors
     */
    public void addValidationSubErrorsByObjectError(List<ObjectError> objectErrors) {
        objectErrors.forEach(objectError -> addValidationSubErrorByObjectError(objectError));
    }

    /**
     * Adds Collision Error by ApiCollisionError
     *
     * @param apiCollisionError
     */
    public void addCollisionSubError(ApiCollisionError apiCollisionError){
        this.errors.add(apiCollisionError);
    }

}
