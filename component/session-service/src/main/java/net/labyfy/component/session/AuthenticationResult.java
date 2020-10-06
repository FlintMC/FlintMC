package net.labyfy.component.session;

/**
 * The result of the {@link SessionService#logIn(String, String)} method.
 */
public enum AuthenticationResult {

  /**
   * Specifies that the log in has successfully been completed.
   */
  SUCCESS,
  /**
   * Specifies that the {@link SessionService} is already logged in in the specified account (email).
   */
  ALREADY_LOGGED_IN,
  /**
   * Specifies that the email and/or password provided to this method are invalid.
   */
  INVALID_CREDENTIALS,
  /**
   * If this result is returned, the mojang auth server is offline and therefore no login is possible.
   */
  AUTH_SERVER_OFFLINE,
  /**
   * Any other error that might occur while logging in and is not specified by this result. Check the log for more
   * details!
   */
  UNKNOWN_ERROR

}