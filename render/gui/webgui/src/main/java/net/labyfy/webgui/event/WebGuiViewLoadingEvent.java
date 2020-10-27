package net.labyfy.webgui.event;

/**
 * Event indicating that the loading status of a {@link net.labyfy.webgui.WebGuiView} has changed.
 */
public interface WebGuiViewLoadingEvent extends WebGuiViewStateChangeEvent {
  /**
   * Retrieves error info about the event.
   *
   * @return The current error info, or {@code null}, if no error occurred
   */
  ErrorInfo errorInfo();

  /**
   * Determines if there was an error.
   *
   * @return {@code true} if an error occurred while loading, {@code false} otherwise
   */
  default boolean hasError() {
    return errorInfo() != null;
  }

  /**
   * Error information for this event.
   */
  class ErrorInfo {
    private final String message;
    private final String domain;
    private final int code;

    /**
     * Constructs a new {@link ErrorInfo}.
     *
     * @param message The error message
     * @param domain  The domain the error originated from
     * @param code    The error code (HTTP code for HTTP errors, anything else for other errors)
     */
    public ErrorInfo(String message, String domain, int code) {
      this.message = message;
      this.domain = domain;
      this.code = code;
    }

    /**
     * Retrieves the error message.
     *
     * @return The error message
     */
    public String getMessage() {
      return message;
    }

    /**
     * Retrieves the domain the error originated from.
     *
     * @return The domain the error originated from
     */
    public String getDomain() {
      return domain;
    }

    /**
     * Retrieves the error code. Might be an HTTP status code for HTTP errors, or anything else for implementation
     * specific errors.
     *
     * @return The error code
     */
    public int getCode() {
      return code;
    }
  }
}
