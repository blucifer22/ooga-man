package ooga.view.views;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    this.primaryView.setAlignment(Pos.CENTER);

    buildScene();
  }

  private void buildScene() {
    Text title = new Text();
    title.setFont(Font.font("Monospaced", 30));
    title.textProperty().bind(languageService.getLocalizedString("pacman"));
    this.primaryView.add(title, 1, 1);
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }
}
