package ooga.view.exceptions;

public class UIServicedException extends Exception {
  private final String errorKey;
  private final String errorInformation;

  public UIServicedException(String errorKey, String errorInformation) {
    this.errorKey = errorKey;
    this.errorInformation = errorInformation;
  }

  public String getErrorKey() {
    return this.errorKey;
  }

  public String getErrorInformation() {
    return this.errorInformation;
  }
}
