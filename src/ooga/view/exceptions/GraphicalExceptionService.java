package ooga.view.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GraphicalExceptionService implements ExceptionService {

  @Override
  public void handleFatalError(Exception subProcessFatalError) {
    handle(AlertType.ERROR, subProcessFatalError.getMessage());
  }

  @Override
  public void handleWarning(Exception warning) {
    handle(AlertType.WARNING, warning.getMessage());
  }

  private void handle(AlertType alertType, String text) {
    Alert a = new Alert(alertType);
    a.setContentText(text);
    a.show();
  }
}
