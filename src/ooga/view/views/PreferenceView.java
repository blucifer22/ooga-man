package ooga.view.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooga.view.internal_api.View;
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
    LabeledComboboxCard languageSelectCard = new LabeledComboboxCard(
        this.serviceProvider,
        "language",
        this.preferenceService.languageSelectionService().getAvailableLanguages(),
        selectedOption -> this.preferenceService.languageSelectionService().setLanguage(selectedOption)
    );
    this.primaryView.add(languageSelectCard, 0, 0);

    LabeledComboboxCard themeSelectCard = new LabeledComboboxCard(
        this.serviceProvider,
        "theme",
        this.preferenceService.themeSelectionService().getAvailableThemes(),
        selectedOption -> this.preferenceService.themeSelectionService().setTheme(selectedOption)
    );
    this.primaryView.add(themeSelectCard, 0, 1);

    Button returnToMenu = new StyledButton(this.serviceProvider, "previousMenu", e -> this.serviceProvider.viewStackManager().unwind());

    VBox backButtonCard = new VBox(
        returnToMenu
    );
    backButtonCard.setAlignment(Pos.CENTER);
    this.primaryView.add(backButtonCard, 0, 2);
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
