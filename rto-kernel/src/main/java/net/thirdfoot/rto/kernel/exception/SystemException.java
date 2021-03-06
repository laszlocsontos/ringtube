package net.thirdfoot.rto.kernel.exception;

/**
 * @author lcsontos
 */
public class SystemException extends RuntimeException {

  public SystemException() {
    super();
  }

  public SystemException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public SystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public SystemException(String message) {
    super(message);
  }

  public SystemException(Throwable cause) {
    super(cause);
  }

}