package ooga.view.views.sceneroots;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooga.view.internal_api.View;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIPreferenceService;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.LabeledComboBoxCard;
import ooga.view.views.components.reusable.StyledButton;

/**
 * Application preference view. Interfaces with a {@link UIPreferenceService} in order to allow the
 * user to set application preferences through a UI. The {@link PreferenceView} allows these
 * preference selections through combo-box menus. Preference changes take effect immediately -- or,
 * at least, the {@link ooga.view.uiservice.PreferenceService} is immediately notified (it may,
 * depending on implementation, delay the preference change at its discretion).
 *
 * @author David Coffman
 */
public class PreferenceView implements ThemedObject, View {

  private final GridPane primaryView;
  private final UIServiceProvider serviceProvider;
  private final UIPreferenceService preferenceService;

  /**
   * Sole {@link PreferenceView} constructor.
   *
   * @param serviceProvider   the {@link ooga.view.uiservice.ServiceProvider} to provide UI services
   *                          as desired
   * @param preferenceService the {@link ooga.view.uiservice.PreferenceService} to provide UI
   *                          preference selection and retrieval services as desired
   */
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

  // Builds the scene by constructing two LabeledComboBoxCards (one for language, one for theme)
  private void buildScene() {
    LabeledComboBoxCard languageSelectCard =
        new LabeledComboBoxCard(
            this.serviceProvider,
            "language",
            this.preferenceService.languageSelectionService().getAvailableLanguages(),
            selectedOption ->
                this.preferenceService.languageSelectionService().setLanguage(selectedOption));
    this.primaryView.add(languageSelectCard, 0, 0);

    LabeledComboBoxCard themeSelectCard =
        new LabeledComboBoxCard(
            this.serviceProvider,
            "theme",
            this.preferenceService.themeSelectionService().getAvailableThemes(),
            selectedOption ->
                this.preferenceService.themeSelectionService().setTheme(selectedOption));
    this.primaryView.add(themeSelectCard, 0, 1);

    Button returnToMenu =
        new StyledButton(
            this.serviceProvider,
            "previousMenu",
            e -> this.serviceProvider.viewStackManager().unwind());

    VBox backButtonCard = new VBox(returnToMenu);
    backButtonCard.setAlignment(Pos.CENTER);
    this.primaryView.add(backButtonCard, 0, 2);
  }

  /**
   * Observer callback. Called when the theme changes. Implementing classes should re-query the
   * {@link ThemeService} for a new {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
    this.primaryView.getStylesheets().clear();
    this.primaryView
        .getStylesheets()
        .add(this.serviceProvider.themeService().getTheme().getStylesheet());
  }

  /**
   * Returns the {@link PreferenceView}'s managed {@link Node}.
   *
   * @return the {@link PreferenceView}'s managed {@link Node}.
   */
  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
  }
}
