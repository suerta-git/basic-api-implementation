package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {RsController.class})
public class RsEventExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(RsEventExceptionHandler.class);

    @ExceptionHandler(RsEventNotValidException.class)
    public ResponseEntity<Error> exceptionHandler(RsEventNotValidException e) {
        logger.error("Here is a " + e.getMessage());
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Error> exceptionHandler(MethodArgumentNotValidException e) {
        logger.error("Here is a invalid param");
        return ResponseEntity.badRequest().body(new Error("invalid param"));
    }
}
