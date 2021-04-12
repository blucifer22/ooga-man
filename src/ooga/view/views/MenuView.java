package ooga.view.views;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import ooga.view.internal_api.View;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;

public class MenuView implements View {

  private final GridPane primaryView;
  private final ThemeService themeService;
  private final LanguageService languageService;

  public MenuView(ThemeService themeService, LanguageService languageService) {
    this.themeService = themeService;
    this.languageService = languageService;
    this.primaryView = new GridPane();
    this.primaryView.setGridLinesVisible(true);
    this.primaryView.setAlignment(Pos.CENTER);

    buildScene();
  }

  private void buildScene() {
    Label title = new Label();
    title.setFont(Font.font("Monospaced", 30));
    title.textProperty().bind(languageService.getLocalizedString("pacman"));
    GridPane.setHalignment(title, HPos.CENTER);
    this.primaryView.add(title, 1, 1);

    Button start = new Button();
    start.textProperty().bind(languageService.getLocalizedString("pacman"));
    start.setFont(Font.font("Monospaced", 30));
    GridPane.setHalignment(start, HPos.CENTER);
    this.primaryView.add(start, 1, 2);
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }
}
