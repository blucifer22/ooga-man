package ooga.view.views.components;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ooga.view.uiservice.UIServiceProvider;

public class StyledButton extends Button {
  public StyledButton(UIServiceProvider serviceProvider, String labelKey,
      EventHandler<MouseEvent> onClickHandler) {
    this.getStyleClass().add("styled-button");
    this.setId("button-" + labelKey);
    this.textProperty().bind(serviceProvider.languageService().getLocalizedString(labelKey));
    this.setOnMouseClicked(e -> {
      onClickHandler.handle(e);
      serviceProvider.audioService().playOnce("button-click");
    });
  }
}
