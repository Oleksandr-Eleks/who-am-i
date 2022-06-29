//package com.eleks.academy.whoami.core.exception;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.Collections;
//
//import static com.eleks.game.components.Constants.FORBIDDEN;
//import static com.eleks.game.components.Constants.NOT_FOUND;
//import static java.util.stream.Collectors.collectingAndThen;
//import static java.util.stream.Collectors.toList;
//
//@ControllerAdvice
//public class GameException extends ResponseEntityExceptionHandler {
//	@ExceptionHandler(RuntimeException.class)
//	public final ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
//		return new ResponseEntity<>(new ErrorResponse("Server Error",
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<ErrorResponse> handleExceptions(Exception ex) {
//		return new ResponseEntity<>(new ErrorResponse("Server Error",
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	@ExceptionHandler(RoomNotFoundException.class)
//	public final ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
//		return new ResponseEntity<>(new ErrorResponse(NOT_FOUND,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(PlayerNotFoundException.class)
//	public final ResponseEntity<ErrorResponse> handlePlayerNotFoundException(RoomNotFoundException ex) {
//		return new ResponseEntity<>(new ErrorResponse(NOT_FOUND,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(DuplicateGameException.class)
//	public final ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateGameException ex) {
//		return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.CONFLICT);
//	}
//
//	@ExceptionHandler(RoomStateException.class)
//	public final ResponseEntity<ErrorResponse> handleRoomStateException(RoomStateException ex) {
//		return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.FORBIDDEN);
//	}
//
//	@ExceptionHandler(AnswerQuestionException.class)
//	public final ResponseEntity<ErrorResponse> handleAnswerGuessingQuestionException(AnswerQuestionException ex) {
//		return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.FORBIDDEN);
//	}
//
//	@ExceptionHandler(TurnException.class)
//	public final ResponseEntity<ErrorResponse> handleWException(TurnException ex) {
//		return new ResponseEntity<>(new ErrorResponse(FORBIDDEN,
//				Collections.singletonList(ex.getLocalizedMessage())),
//				HttpStatus.FORBIDDEN);
//	}
//
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//																  HttpHeaders headers, HttpStatus status,
//																  WebRequest request) {
//		return ex.getBindingResult().getAllErrors()
//				.stream()
//				.map(ObjectError::getDefaultMessage)
//				.collect(collectingAndThen(
//						toList(),
//						details -> ResponseEntity.badRequest()
//								.body(new ErrorResponse("Validation failed!", details))
//				));
//	}
//
//}
