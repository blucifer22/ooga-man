package ooga.view.exceptions;

import java.util.Arrays;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ooga.view.language.api.LanguageService;

/**
 * Concrete implementation of {@link ExceptionService} used for displaying exceptions to the user
 * without unintentionally crashing the application. Typically called in a <code>catch</code>
 * block:
 *
 * <code>
 * try { File f = new File("path/to/nonexistent/file.test"); functionThatThrowsIOException(f); }
 * catch (IOException e) { exceptionService.handleWarning(new UIServicedException("missingFile"));
 * }
 * </code>
 *
 * @author David Coffman
 */
public class GraphicalExceptionService implements ExceptionService {

  private static final String MUTE_WARNINGS_FALLBACK = "Mute Warnings";
  private static final String MUTE_WARNINGS_TYPE_CODE = "U";
  private LanguageService languageService;
  private boolean mute;

  /**
   * Sets the {@link LanguageService} for this object. The {@link LanguageService} is thereafter
   * used by the {@link GraphicalExceptionService} to localize error messages.
   *
   * @param languageService the {@link LanguageService} to be used for lookup
   */
  public void setLanguageService(LanguageService languageService) {
    this.languageService = languageService;
  }

  /**
   * Handles highly severe, application-terminating-ly critical errors, such as the absence of all
   * resource bundles. This implementation <b>DOES</b> elect to terminate the application on a fatal
   * error.
   *
   * @param subProcessFatalError the extremely severe error to handle
   */
  @Override
  public void handleFatalError(UIServicedException subProcessFatalError) {
    handle(AlertType.ERROR, getMessage(subProcessFatalError));
  }

  /**
   * Handles less severe, non-fatal errors, such as missing image assets.
   *
   * @param warning the warning to handle
   */
  @Override
  public void handleWarning(UIServicedException warning) {
    handle(AlertType.WARNING, getMessage(warning));
  }

  // retrieves an error message from the LanguageService, if initialized
  private String getMessage(UIServicedException exception) {
    if (languageService != null) {
      try {
        return String.format(
            languageService.getLocalizedString(exception.getErrorKey()).get(),
            (Object[]) exception.getErrorInformation());
      } catch (Exception ignored) {
      }
    }
    // fallback message; cannot be refactored to a resource bundle!
    // this message is shown when resource bundle loading fails
    return String.format(
        "Encountered error %s (additional information %s). Error occurred before"
            + " localization settings could be applied, or a localization error occurred.",
        exception.getErrorKey(), Arrays.toString(exception.getErrorInformation()));
  }

  // Handles the error by displaying it to the user in an Alert box.
  private void handle(AlertType alertType, String text) {
    Alert a = new Alert(alertType);
    a.setContentText(text);
    if (alertType == AlertType.ERROR) {
      a.showAndWait();
      Platform.exit();
    } else if (!mute) {
      String muteButtonText =
          languageService != null
              ? languageService.getLocalizedString("muteWarnings").get()
              : MUTE_WARNINGS_FALLBACK;
      a.getButtonTypes()
          .add(
              new ButtonType(!muteButtonText.equals("") ? muteButtonText : MUTE_WARNINGS_FALLBACK));
      a.setOnCloseRequest(
          e -> {
            if (a.getResult().getButtonData().getTypeCode().equals(MUTE_WARNINGS_TYPE_CODE)) {
              mute = true;
            }
            a.close();
          });
      a.show();
      ((Stage) a.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
    }
  }
}
