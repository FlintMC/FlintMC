package net.flintmc.transform.exceptions;

/**
 * The <code>ClassTransformException</code> will be thrown if one or more
 * class transformations failed.
 */
public class ClassTransformException extends Exception {

  /**
   * Constructs a new exception with {@code null} as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   */
  public ClassTransformException() {
    super();
  }

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param   message   The detail message. The detail message is saved for
   *          later retrieval by the {@link #getMessage()} method.
   */
  public ClassTransformException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message and
   * cause.  <p>Note that the detail message associated with
   * {@code cause} is <i>not</i> automatically incorporated in
   * this exception's detail message.
   *
   * @param  message The detail message (which is saved for later retrieval
   *         by the {@link #getMessage()} method).
   * @param  cause The cause (which is saved for later retrieval by the
   *         {@link #getCause()} method).  (A {@code null} value is
   *         permitted, and indicates that the cause is nonexistent or
   *         unknown.)
   */
  public ClassTransformException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new exception with the specified cause and a detail
   * message of {@code (cause==null ? null : cause.toString())} (which
   * typically contains the class and detail message of {@code cause}).
   * This constructor is useful for exceptions that are little more than
   * wrappers for other throwables (for example, {@link
   * java.security.PrivilegedActionException}).
   *
   * @param  cause The cause (which is saved for later retrieval by the
   *         {@link #getCause()} method).  (A {@code null} value is
   *         permitted, and indicates that the cause is nonexistent or
   *         unknown.)
   */
  public ClassTransformException(Throwable cause) {
    super(cause);
  }
}
