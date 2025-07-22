package study.spring.splearn.adapter;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.spring.splearn.domain.member.DuplicateEmailException;
import study.spring.splearn.domain.member.DuplicateProfileException;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
  public ProblemDetail duplicateEmailExceptionHandler(DuplicateEmailException exception) {
    return getProblemDetail(HttpStatus.CONFLICT, exception);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception exception) {
    return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception);
  }

  private static ProblemDetail getProblemDetail(HttpStatus status, Exception exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    problemDetail.setProperty("exception", exception.getClass().getSimpleName());

    return problemDetail;
  }
}
