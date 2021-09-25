/*
 * 
 */
package com.advatix.partner.commons.exceptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.hibernate.LazyInitializationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.advatix.partner.commons.logger.LogManager;
import com.advatix.partner.commons.logger.Logger;
import com.advatix.partner.commons.logger.MessageType;
import com.advatix.partner.commons.utils.Constant;
import com.advatix.partner.commons.utils.RestResponse;
import com.advatix.partner.commons.utils.RestUtils;
import com.advatix.partner.commons.utils.Utils;

/**
 * The Class RestExceptionHandler.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /** The logger. */
  private Logger LOGGER = Logger.getLogger(RestExceptionHandler.class);

  @Autowired
  private Environment env;

  /** The ezr mail service. */
  // @Autowired
  // private EZRMailService ezrMailService;

  /** The environment. */
  @Value("${application.base.url}")
  private String environment;

  @Value("${spring.servlet.multipart.max-file-size}")
  private String maxFileSize;

  /**
   * Handle method argument not valid.
   *
   * @param ex the ex
   * @param headers the headers
   * @param status the status
   * @param request the request
   * @return the response entity
   */
  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.web.servlet.mvc.method.annotation. ResponseEntityExceptionHandler
   * #handleMethodArgumentNotValid(org.springframework .web.bind.MethodArgumentNotValidException,
   * org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
   * org.springframework.web.context.request.WebRequest)
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(
        new RestResponse<>(convertConstraintViolation(ex), null, Boolean.FALSE, status.value()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Convert constraint violation.
   *
   * @param ex the ex
   * @return the string
   */
  protected String convertConstraintViolation(MethodArgumentNotValidException ex) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<String> errorMessages = new ArrayList<>();
    for (FieldError c : fieldErrors) {
      errorMessages.add(c.getField().concat(" - ")
          .concat(c.getDefaultMessage().charAt(0) == '{'
              ? env.resolvePlaceholders("$".concat(c.getDefaultMessage()))
              : c.getDefaultMessage()));
    }
    sendEmail(
        "RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
            + LOGGER.getValue(Constant.REQUEST_ID) + "<br>" + errorMessages.toString(),
        environment + ": MethodArgumentNotValidException");
    return String.join(", ", errorMessages);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RestResponse<?>> handle(
      ConstraintViolationException constraintViolationException) {
    Set<ConstraintViolation<?>> violations = null;
    String errorMessage = "";
    if (!violations.isEmpty()) {
      List<String> errorMessages = new ArrayList<>();
      violations.forEach(violation -> errorMessages.add(violation.getMessage().charAt(0) == '{'
          ? env.resolvePlaceholders("$".concat(violation.getMessage()))
          : violation.getMessage()));
      errorMessage = String.join(", ", errorMessages);
    } else {
      errorMessage = "ConstraintViolationException occured.";
    }
    return RestUtils.errorResponse(errorMessage, null, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle Data Integrity Violation exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {DataIntegrityViolationException.class})
  protected ResponseEntity<RestResponse<?>> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {
    LOGGER.error(ex.getRootCause().getLocalizedMessage(), ex);
    LogManager.error(getClass(), ex.getRootCause().getLocalizedMessage(), MessageType.Error);
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br>" + ex.getRootCause().getLocalizedMessage(),
        environment + ": DataIntegrityViolationException");
    return RestUtils.errorResponse(ex.getRootCause().getLocalizedMessage(),
        ex.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Handle lazy initialization exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {LazyInitializationException.class})
  protected ResponseEntity<?> handleLazyInitializationException(LazyInitializationException ex,
      WebRequest request) {
    LOGGER.error(ex.getLocalizedMessage(), ex);
    LogManager.error(getClass(), ex.getLocalizedMessage(), MessageType.Error);
    String trace = Utils.displayErrorForWeb(ex.getStackTrace());
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br/>" + ex.getLocalizedMessage()
        + "<br>StackTrace:<br/>" + trace, environment + ": Exception");
    return RestUtils.errorResponse(ex.getLocalizedMessage(), ex.getLocalizedMessage(),
        HttpStatus.CONFLICT);
  }

  /**
   * Handle incorrect result size data access exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {IncorrectResultSizeDataAccessException.class})
  protected ResponseEntity<RestResponse<?>> handleIncorrectResultSizeDataAccessException(
      IncorrectResultSizeDataAccessException ex, WebRequest request) {
    LOGGER.error(ex.getRootCause().getLocalizedMessage(), ex);
    LogManager.error(getClass(), ex.getRootCause().getLocalizedMessage(), MessageType.Error);
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br>" + ex.getRootCause().getLocalizedMessage(),
        environment + ": IncorrectResultSizeDataAccessException");
    return RestUtils.errorResponse(ex.getRootCause().getLocalizedMessage(),
        ex.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Handle SQL exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {SQLException.class})
  protected ResponseEntity<RestResponse<?>> handleSQLException(SQLException ex,
      WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getLocalizedMessage(), MessageType.Error);
    sendEmail(
        "RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
            + LOGGER.getValue(Constant.REQUEST_ID) + "<br>" + ex.getLocalizedMessage(),
        environment + ": SQLException");
    return RestUtils.errorResponse(ex.getLocalizedMessage(), ex.getLocalizedMessage(),
        HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Handle unknown exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<RestResponse<?>> handleUnknownException(Exception ex,
      WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    LOGGER.error(ex.getClass().getName());
    String trace = Utils.displayErrorForWeb(ex.getStackTrace());
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br/>" + ex.getLocalizedMessage()
        + "<br>StackTrace:<br/>" + trace, environment + ": Exception");
    return RestUtils.errorResponse(ex.getMessage(), ex.getLocalizedMessage(),
        BaseException.DEFAULT_HTTP_STATUS);
  }

  /**
   * Handle null pointer exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {NullPointerException.class})
  protected ResponseEntity<RestResponse<?>> handleNullPointerException(NullPointerException ex,
      WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    LOGGER.error(ex.getClass().getName());
    String trace = Utils.displayErrorForWeb(ex.getStackTrace());
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br/>" + ex.getLocalizedMessage()
        + "<br>StackTrace:<br/>" + trace, environment + ": NullPointerException");
    return RestUtils.errorResponse(ex.getMessage(), ex.getLocalizedMessage(),
        BaseException.DEFAULT_HTTP_STATUS);
  }

  /**
   * Handle max upload size exceeded exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {MultipartException.class})
  protected ResponseEntity<RestResponse<?>> handleMaxUploadSizeExceededException(
      MultipartException ex, WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    LOGGER.error(ex.getClass().getName());
    String trace = Utils.displayErrorForWeb(ex.getStackTrace());
    sendEmail("RequestKey: " + LOGGER.getValue(Constant.REQUEST_KEY) + "<br>RequestId: "
        + LOGGER.getValue(Constant.REQUEST_ID) + "<br/>" + ex.getLocalizedMessage()
        + "<br>StackTrace:<br/>" + trace, environment + ": MultipartException");
    return RestUtils.errorResponse(ex.getMessage(), ex.getLocalizedMessage(),
        BaseException.DEFAULT_HTTP_STATUS);
  }

  /**
   * Handle base exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {BaseException.class})
  protected ResponseEntity<RestResponse<?>> handleBaseException(BaseException ex,
      WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    return RestUtils.errorResponse(ex.getMessage(), ex.getData(), ex.getHttpStatus());
  }

  /**
   * Handle entity not found exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<RestResponse<?>> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    return RestUtils.errorResponse(ex.getMessage(), ex.getHttpStatus());
  }

  /**
   * Handle entity already exists exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(value = {EntityAlreadyExistsException.class})
  protected ResponseEntity<RestResponse<?>> handleEntityAlreadyExistsException(
      EntityAlreadyExistsException ex, WebRequest request) {
    LOGGER.error(ex.getMessage(), ex);
    LogManager.error(getClass(), ex.getMessage(), MessageType.Error);
    return RestUtils.errorResponse(ex.getMessage(), ex.getHttpStatus());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<RestResponse<?>> handleMaxSizeException(MaxUploadSizeExceededException exc,
      HttpServletRequest request, HttpServletResponse response) {
    LogManager.info(getClass(), "FileUploadException: " + exc.getMessage(), MessageType.ApiRequest);
    return RestUtils.errorResponse("File size cannot exceed from " + maxFileSize,
        HttpStatus.PAYLOAD_TOO_LARGE);
  }

  @ExceptionHandler(value = {UnexpectedRollbackException.class})
  protected ResponseEntity<RestResponse<?>> unexpectedRollbackException(
      UnexpectedRollbackException ex, WebRequest request) {
    LOGGER.error(ex.getLocalizedMessage(), ex);
    LogManager.error(getClass(), ex.getLocalizedMessage(), MessageType.Error);
    return RestUtils.errorResponse(ex.getLocalizedMessage(), ex.getLocalizedMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private void sendEmail(String message, String subject) {
    // try {
    // ezrMailService.sendException(message, subject, "ankush@brainworkindia.net");
    // } catch (UnsupportedEncodingException e) {
    // }
  }

}
