package ar.com.meli.cupon.exceptions;

import ar.com.meli.cupon.dto.ResponseError;
import ar.com.meli.cupon.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Messages messages;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUncontrolledException(Exception e, WebRequest req){
        logger.error(messages.get("globalexceptionhandler.uncontroled.error.code"), e);
        return new ResponseEntity(new ResponseError(messages.get("globalexceptionhandler.uncontroled.error.code"), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
