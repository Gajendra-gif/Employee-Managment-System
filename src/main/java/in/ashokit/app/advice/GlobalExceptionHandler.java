package in.ashokit.app.advice;

import in.ashokit.app.Exception.ResourceNotFound;
import in.ashokit.app.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String , String> notValidExceptionHandle(MethodArgumentNotValidException ex){

        log.warn("Request Validation Failed ");

        Map<String , String> mapError = new HashMap<>();

        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        errorList.forEach(error -> {
            mapError.put(error.getField() , error.getDefaultMessage());
        });

        return mapError;
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFound ex , HttpServletRequest request){

        log.warn("Resource not found for URI {}: {}",
                request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStausCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage(ex.getMessage());
        errorResponse.setRequestURI(request.getRequestURI());
        errorResponse.setTimeStamp(Instant.now());

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);
    }

}
