package com.eleks.academy.whoami.configuration;

import com.eleks.academy.whoami.core.exception.*;
import com.eleks.academy.whoami.model.response.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

import static com.eleks.academy.whoami.enums.Constants.FORBIDDEN;
import static com.eleks.academy.whoami.enums.Constants.NOT_FOUND;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class GameControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleGameException(GameException gameException) {
        return gameException::getMessage;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(collectingAndThen(
                        toList(),
                        details -> ResponseEntity.badRequest()
                                .body(new ErrorResponse("Validation failed!", details))
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorResponse("Server Error",
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleExceptions(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Server Error",
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleGameNotFoundException(GameNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handlePlayerNotFoundException(GameNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateGameException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateGameException ex) {
        return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GameStateException.class)
    public final ResponseEntity<ErrorResponse> handleGameStateException(GameStateException ex) {
        return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AnswerQuestionException.class)
    public final ResponseEntity<ErrorResponse> handleAnswerGuessingQuestionException(AnswerQuestionException ex) {
        return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TurnException.class)
    public final ResponseEntity<ErrorResponse> handleWException(TurnException ex) {
        return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
                Collections.singletonList(ex.getLocalizedMessage())),
                HttpStatus.FORBIDDEN);
    }

}
