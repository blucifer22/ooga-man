package ooga.view.views.sceneroots;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.internal_api.View;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.StyledButton;

public class MenuView implements View, ThemedObject {

  private final GridPane primaryView;
  private final MainMenuResponder menuResponder;
  private final UIServiceProvider serviceProvider;

  public MenuView(UIServiceProvider serviceProvider, MainMenuResponder menuResponder) {
    this.serviceProvider = serviceProvider;
    this.menuResponder = menuResponder;
    this.primaryView = new GridPane();
    this.primaryView.setGridLinesVisible(true);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.getStyleClass().add("view");
    this.serviceProvider.themeService().addThemedObject(this);
    buildScene();
  }

  private void buildScene() {
    Label title = new Label();
    title.getStyleClass().add("menu-title");
    title.textProperty().bind(serviceProvider.languageService().getLocalizedString("pacman"));
    GridPane.setHalignment(title, HPos.CENTER);
    this.primaryView.add(title, 0, 0);

    VBox menuButtons = new VBox(
        menuButton("startGame", e -> this.menuResponder.startGame()),
        menuButton("openLevelBuilder", e -> this.menuResponder.openLevelBuilder()),
        menuButton("openPreferences", e -> this.menuResponder.openPreferences())
    );
    menuButtons.setAlignment(Pos.CENTER);
    menuButtons.getStyleClass().add("menu-button-box");
    this.primaryView.add(menuButtons, 0, 1);
  }

  private Button menuButton(String labelKey, EventHandler<MouseEvent> onClickHandler) {
    Button button = new StyledButton(this.serviceProvider, labelKey, onClickHandler);
    button.setMaxWidth(Double.MAX_VALUE);
    return button;
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView.getStylesheets().add(serviceProvider.themeService().getTheme().getStylesheet());
  }
}
