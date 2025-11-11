package com.agent_java.mcp_server.exception;

import com.agent_java.mcp_server.viewmodel.ErrorResponseVm;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseVm> handleNotFound(ResourceNotFoundException exception) {
        var err = new ErrorResponseVm(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage());
        return new ResponseEntity(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseVm> handleBadRequest(BadRequestException exception) {
        var err = new ErrorResponseVm(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage());
        return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseVm> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            map.put(error.getField(), error.getDefaultMessage());
        });
        var err = new ErrorResponseVm(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                toString(map));
        return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns {@code String} representation of a {@code Map}.
     * <p/>
     * This method is equivalent to this Kotlin code:
     * <pre>
     * val map = mapOf("key1" to "value1", "key2" to "value2")
     * println(map.toList().toString())
     * // Output: [(key1, value1), (key2, value2)]
     * </pre>
     * This method is a modification of {@link java.util.AbstractMap#toString()}.
     *
     * @see java.util.AbstractMap#toString()
     * @param map a Map
     * @return a string representation of input map
     */
    private static String toString(Map map) {
        Iterator<Map.Entry> i = map.entrySet().iterator();
        if (!i.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            Map.Entry e = i.next();
            sb.append('(').append(e.getKey()).append(", ").append(e.getValue()).append(')');
            if (!i.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseVm> handleGeneralExceptions(Exception exception) {
        var err = new ErrorResponseVm(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong" // exception.getMessage() // Hide message
        );
        Logger.getLogger(GlobalExceptionHandler.class.getName()).log(Level.SEVERE, null, exception.getMessage());
        return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
