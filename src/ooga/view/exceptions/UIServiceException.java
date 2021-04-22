package ooga.view.exceptions;

public class UIServiceException extends Exception {
  private final String errorKey;
  private final String errorInformation;

  public UIServiceException(String errorKey, String errorInformation) {
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
