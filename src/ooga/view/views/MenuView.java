package ooga.view.views;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.internal_api.View;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

public class MenuView implements View, ThemedObject {

  private final GridPane primaryView;
  private final ThemeService themeService;
  private final LanguageService languageService;
  private final MainMenuResponder menuResponder;

  public MenuView(MainMenuResponder menuResponder, ThemeService themeService,
      LanguageService languageService) {
    this.menuResponder = menuResponder;
    this.themeService = themeService;
    this.languageService = languageService;
    this.primaryView = new GridPane();
    this.primaryView.setGridLinesVisible(true);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.getStyleClass().add("view");
    this.themeService.addThemedObject(this);
    buildScene();
  }

  private void buildScene() {
    Label title = new Label();
    title.getStyleClass().add("menu-title");
    title.textProperty().bind(languageService.getLocalizedString("pacman"));
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
    Button start = new Button();
    start.getStyleClass().add("menu-button");
    start.textProperty().bind(languageService.getLocalizedString(labelKey));
    start.setOnMouseClicked(onClickHandler);
    start.setMaxWidth(Double.MAX_VALUE);
    return start;
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView.getStylesheets().add(themeService.getTheme().getStylesheet());
  }
}
