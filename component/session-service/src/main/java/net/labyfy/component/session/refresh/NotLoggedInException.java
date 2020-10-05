package net.labyfy.component.session.refresh;

public class NotLoggedInException extends RuntimeException {

  public static final NotLoggedInException INSTANCE = new NotLoggedInException();

  private NotLoggedInException() {
  }

}
