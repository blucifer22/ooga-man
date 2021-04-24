package ooga.view.exceptions;

import java.util.Arrays;
import java.util.Stack;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ooga.view.language.api.LanguageService;

public class GraphicalExceptionService implements ExceptionService {

  private static final String MUTE_WARNINGS_FALLBACK = "Mute Warnings";
  private static final String MUTE_WARNINGS_TYPE_CODE = "U";
  private LanguageService languageService;
  private boolean mute;

  public void setLanguageService(LanguageService languageService) {
    this.languageService = languageService;
  }

  @Override
  public void handleFatalError(UIServicedException subProcessFatalError) {
    handle(AlertType.ERROR, getMessage(subProcessFatalError));
  }

  @Override
  public void handleWarning(UIServicedException warning) {
    handle(AlertType.WARNING, getMessage(warning));
  }

  private String getMessage(UIServicedException exception) {
    if (languageService != null) {
      try {
        return String.format(languageService.getLocalizedString(exception.getErrorKey()).get(),
            (Object[]) exception.getErrorInformation());
      } catch (Exception ignored) {
      }
    }
    // fallback message; cannot be refactored to a resource bundle!
    // this message is shown when resource bundle loading fails
    return String.format("Encountered error %s (additional information %s). Error occurred before"
            + " localization settings could be applied, or a localization error occurred.",
        exception.getErrorKey(), Arrays.toString(exception.getErrorInformation()));
  }

  private void handle(AlertType alertType, String text) {
    Alert a = new Alert(alertType);
    a.setContentText(text);
    if (alertType == AlertType.ERROR) {
      a.showAndWait();
      Platform.exit();
    } else if (!mute) {
      String muteButtonText = languageService != null ?
          languageService.getLocalizedString("muteWarnings").get() : MUTE_WARNINGS_FALLBACK;
      a.getButtonTypes().add(new ButtonType(!muteButtonText.equals("") ? muteButtonText : MUTE_WARNINGS_FALLBACK));
      a.setOnCloseRequest(e -> {
        if(a.getResult().getButtonData().getTypeCode().equals(MUTE_WARNINGS_TYPE_CODE)) {
          mute = true;
        }
        a.close();
      });
      a.show();
      ((Stage) a.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
    }
  }
}
