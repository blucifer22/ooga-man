package ooga.view.views.components.reusable;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ooga.view.uiservice.UIServiceProvider;

/**
 * Small extension to the {@link Button} class to allow for quick construction of styled buttons
 * with labels bound to a {@link ooga.view.language.api.LanguageService}'s managed string property.
 *
 * @author David Coffman
 */
public class StyledButton extends Button {

  /**
   * Instantiates a {@link StyledButton}.
   *
   * @param serviceProvider the {@link UIServiceProvider} whose {@link ooga.view.language.api.LanguageService}
   *                        should be queried for this {@link StyledButton}'s label text property
   * @param labelKey        the key with which to query for a label string property
   * @param onClickHandler  the button's on-click handler
   */
  public StyledButton(
      UIServiceProvider serviceProvider, String labelKey, EventHandler<MouseEvent> onClickHandler) {
    this.getStyleClass().add("styled-button");
    this.setId("button-" + labelKey);
    this.textProperty().bind(serviceProvider.languageService().getLocalizedString(labelKey));
    this.setOnMouseClicked(
        e -> {
          onClickHandler.handle(e);
          serviceProvider.audioService().playOnce("button-click");
        });
  }
}
