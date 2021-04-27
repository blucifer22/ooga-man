package ooga.view.exceptions;

/**
 * Exception class for use by UI classes when interacting with an {@link ExceptionService}. Usage
 * example below:
 *
 * <code>
 *   try {
 *     File f = new File("path/to/nonexistent/file.test");
 *     functionThatThrowsIOException(f);
 *   } catch (IOException e) {
 *     exceptionService.handleWarning(new UIServicedException("missingFile"));
 *   }
 * </code>
 *
 * See {@link ExceptionService} for additional documentation.
 *
 * @author David Coffman
 */
public class UIServicedException extends Exception {

  private final String errorKey;
  private final String[] errorInformation;

  /**
   * Sole {@link UIServicedException} constructor.
   *
   * @param errorKey the error's error key (type)
   * @param errorInformation additional information for use in formatting an error message for
   *                         the user
   */
  public UIServicedException(String errorKey, String... errorInformation) {
    this.errorKey = errorKey;
    this.errorInformation = new String[errorInformation.length];
    System.arraycopy(errorInformation, 0, this.errorInformation, 0, errorInformation.length);
  }

  /**
   * Returns the error key. Used by {@link GraphicalExceptionService} to either directly display
   * an error to a user or to look up the localization for the error in the
   * {@link ooga.view.language.api.LanguageSelectionService}.
   *
   * @return the error key
   */
  public String getErrorKey() {
    return this.errorKey;
  }

  /**
   * Returns any additional information for use in formatting an error message for the user.
   * Should correspond with the format string matching the error key; this information will
   * otherwise not be displayed.
   *
   * @return additional information for use in formatting an error message for the user.
   */
  public String[] getErrorInformation() {
    return this.errorInformation;
  }
}
