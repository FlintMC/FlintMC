package net.flintmc.util.session;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * The result of the {@link SessionService#refreshToken()} method.
 */
public interface RefreshTokenResult {

  /**
   * Retrieves the type of result.
   *
   * @return The non-null type
   */
  ResultType getType();

  /**
   * Retrieves the error message that has been provided by the session server for a failed result.
   *
   * @return The error message or {@code null} if the request was either success or no error message
   * has been sent
   */
  String getErrorMessage();

  /**
   * Types of results for the {@link RefreshTokenResult}.
   */
  enum ResultType {

    /**
     * The result was success.
     */
    SUCCESS,
    /**
     * The token has been tried to be refreshed without being logged in into any account in the
     * {@link SessionService}.
     */
    NOT_LOGGED_IN,
    /**
     * Any other error sent by the server which is not specifically specified, the {@link
     * #getErrorMessage()} might contain more specific information.
     */
    OTHER
  }

  /**
   * Factory for the {@link RefreshTokenResult}.
   */
  @AssistedFactory(RefreshTokenResult.class)
  interface Factory {

    /**
     * Creates a new result with the given type and message.
     *
     * @param resultType   The non-null type of the result
     * @param errorMessage The non-null error message of the result
     * @return The new non-null result with the type and message
     */
    RefreshTokenResult create(
        @Assisted("type") ResultType resultType, @Assisted("errorMessage") String errorMessage);

    /**
     * Creates a new result without an error message.
     *
     * @param resultType The non-null type of the result
     * @return The new non-null result with the type and {@code null} as the error message
     */
    RefreshTokenResult createUnknown(@Assisted("type") ResultType resultType);
  }
}
