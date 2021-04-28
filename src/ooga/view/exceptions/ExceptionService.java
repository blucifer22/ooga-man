package ooga.view.exceptions;

/**
 * UI service for displaying exceptions to the user without unintentionally crashing the
 * application. Typically called in a <code>catch</code> block:
 *
 * <code>
 * try { File f = new File("path/to/nonexistent/file.test"); functionThatThrowsIOException(f); }
 * catch (IOException e) { exceptionService.handleWarning(new UIServicedException("missingFile"));
 * }
 * </code>
 *
 * @author David Coffman
 */
public interface ExceptionService {

  /**
   * Handles highly severe, application-terminating-ly critical errors, such as the absence of all
   * resource bundles. {@link ExceptionService} implementing classes <b>MAY ELECT TO TERMINATE THE
   * APPLICATION AFTER THE USER ACKNOWLEDGES THE FATAL ERROR</b>.
   *
   * @param subProcessFatalError the extremely severe error to handle
   */
  void handleFatalError(UIServicedException subProcessFatalError);

  /**
   * Handles less severe, non-fatal errors, such as missing image assets.
   *
   * @param warning the warning to handle
   */
  void handleWarning(UIServicedException warning);
}
