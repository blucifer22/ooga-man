package ooga.view.views;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooga.view.internal_api.PreferenceResponder;
import ooga.view.internal_api.View;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

public class PreferenceView implements ThemedObject, View {

  private final GridPane primaryView;
  private final ThemeService themeService;
  private final LanguageService languageService;
  private final PreferenceResponder preferenceResponder;
  private final ResourceBundle languageManifest;

  public PreferenceView(PreferenceResponder preferenceResponder, ThemeService themeService,
      LanguageService languageService) {
    this.preferenceResponder = preferenceResponder;
    this.languageManifest = ResourceBundle.getBundle("resources.languages.manifest");
    this.themeService = themeService;
    this.themeService.addThemedObject(this);
    this.languageService = languageService;
    this.primaryView = new GridPane();
    this.primaryView.setGridLinesVisible(true);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.getStyleClass().add("view");
    this.onThemeChange();

    buildScene();
  }

  private void buildScene() {
    Label langDropdownLabel = new Label();
    langDropdownLabel.textProperty().bind(languageService.getLocalizedString("language"));
    langDropdownLabel.getStyleClass().add("dropdown-label");
    this.primaryView.add(langDropdownLabel, 0, 0);

    ComboBox<String> langDropdown = new ComboBox<>();

    for (String key: languageManifest.keySet()) {
      langDropdown.getItems().add(key);
    }

    langDropdown.setOnAction(e -> this.preferenceResponder.setLanguage(langDropdown.getValue()));

    VBox labeledLangDropdown = new VBox(
        langDropdownLabel,
        langDropdown
    );
    this.primaryView.add(labeledLangDropdown, 0, 1);
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView.getStylesheets().add(themeService.getTheme().getStylesheet());
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }
}
