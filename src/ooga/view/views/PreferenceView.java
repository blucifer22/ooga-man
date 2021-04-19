package ooga.view.views;

import java.util.ArrayList;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ooga.view.internal_api.PreferenceResponder;
import ooga.view.internal_api.View;
import ooga.view.internal_api.ViewStackManager;
import ooga.view.language.api.LanguageSelectionService;
import ooga.view.language.api.LanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIPreferenceService;
import ooga.view.uiservice.UIServiceProvider;

public class PreferenceView implements ThemedObject, View {

  private final GridPane primaryView;
  private final UIServiceProvider serviceProvider;
  private final UIPreferenceService preferenceService;

  public PreferenceView(UIServiceProvider serviceProvider, UIPreferenceService preferenceService) {
    this.preferenceService = preferenceService;
    this.serviceProvider = serviceProvider;
    this.primaryView = new GridPane();
    this.primaryView.setGridLinesVisible(true);
    this.primaryView.setAlignment(Pos.CENTER);
    this.primaryView.getStyleClass().addAll("view", "card-pane");
    this.serviceProvider.themeService().addThemedObject(this);

    buildScene();
  }

  private void buildScene() {
    Label langDropdownLabel = new Label();
    langDropdownLabel.textProperty().bind(this.serviceProvider.languageService().getLocalizedString("language"));
    langDropdownLabel.getStyleClass().add("dropdown-label");
    langDropdownLabel.setId("menu-label-lang-select");

    // FIXME: remove typecast
    Map<String, String> availableLanguages =
        this.preferenceService.languageSelectionService().getAvailableLanguages();

    ArrayList<Pair<String, String>> dropdownOptions = new ArrayList<>();
    for (String key : availableLanguages.keySet()) {
      dropdownOptions.add(new Pair<>(key, availableLanguages.get(key)) {
        @Override
        public String toString() {
          return this.getValue();
        }
      });
    }

    ComboBox<Pair<String, String>> langDropdown = new ComboBox<>();
    langDropdown.getItems().addAll(dropdownOptions);
    langDropdown.setId("menu-combo-lang-select");
    langDropdown
        .setOnAction(e -> this.preferenceService.languageSelectionService().setLanguage(langDropdown.getValue().getKey()));

    VBox labeledLangDropdown = new VBox(
        langDropdownLabel,
        langDropdown
    );
    labeledLangDropdown.getStyleClass().add("card");
    this.primaryView.add(labeledLangDropdown, 0, 0);

    Button returnToMenu = new Button();
    returnToMenu.textProperty().bind(this.serviceProvider.languageService().getLocalizedString("previousMenu"));
    returnToMenu.setOnMouseClicked(e -> this.serviceProvider.viewStackManager().unwind());
    returnToMenu.getStyleClass().add("menu-button");
    returnToMenu.setId("menu-button-previousMenu");

    VBox backButtonCard = new VBox(
        returnToMenu
    );
    backButtonCard.setAlignment(Pos.CENTER);
    this.primaryView.add(backButtonCard, 0, 1);
  }

  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView.getStylesheets().add(this.serviceProvider.themeService().getTheme().getStylesheet());
  }

  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }
}
