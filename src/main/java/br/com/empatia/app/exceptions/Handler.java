package br.com.empatia.app.exceptions;

import br.com.empatia.app.resources.ApiErrorResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestControllerAdvice
public class Handler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var httpStatus = status.value();
        var message = "One or more fields are invalids";
        var path = request.getDescription(false).substring(4);

        var response = new ApiErrorResource(httpStatus, message, path);

        var fields = new ArrayList<String>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            var fieldName = ((FieldError) error).getField();
            var msg = error.getDefaultMessage();
            fields.add(fieldName + " - " + msg);
        }

        response.setFields(fields);

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    private ResponseEntity<ApiErrorResource> buildException(String msg, HttpServletRequest request, HttpStatus status) {
        var responseBody = new ApiErrorResource(status.value(), msg, request.getRequestURI());
        return ResponseEntity.status(status).body(responseBody);
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<ApiErrorResource> invalidAttributeException(InvalidAttributeException ex, HttpServletRequest request) {
        return buildException(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResource> businessRuleException(BusinessRuleException ex, HttpServletRequest request) {
        return buildException(ex.getMessage(), request, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResource> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return buildException(ex.getMessage(), request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResource> maxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return buildException(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResource> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildException(ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }
}
