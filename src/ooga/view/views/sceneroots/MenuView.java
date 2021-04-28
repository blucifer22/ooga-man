package ooga.view.views.sceneroots;

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
import ooga.view.internal_api.Renderable;
import ooga.view.internal_api.View;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.StyledButton;

/**
 * A main menu view implementation. Provides buttons to trigger the actions in {@link
 * MainMenuResponder} -- start the game, open the level builder, open the preferences view.
 *
 * @author David Coffman
 */
public class MenuView implements View, ThemedObject {

  private final GridPane primaryView;
  private final MainMenuResponder menuResponder;
  private final UIServiceProvider serviceProvider;

  /**
   * Sole constructor for {@link MenuView}.
   *
   * @param serviceProvider the {@link ooga.view.uiservice.ServiceProvider} to provide UI services
   *                        as desired
   * @param menuResponder   the {@link MainMenuResponder} to handle this view's action responses
   */
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

  // Add the three buttons to the scene.
  private void buildScene() {
    Label title = new Label();
    title.getStyleClass().add("menu-title");
    title.textProperty().bind(serviceProvider.languageService().getLocalizedString("pacman"));
    GridPane.setHalignment(title, HPos.CENTER);
    this.primaryView.add(title, 0, 0);

    VBox menuButtons =
        new VBox(
            menuButton("startGame", e -> this.menuResponder.startGame()),
            menuButton("openLevelBuilder", e -> this.menuResponder.openLevelBuilder()),
            menuButton("openPreferences", e -> this.menuResponder.openPreferences()));
    menuButtons.setAlignment(Pos.CENTER);
    menuButtons.getStyleClass().add("menu-button-box");
    this.primaryView.add(menuButtons, 0, 1);
  }

  // shorthand for an infinitely-horizontal-expanding menu button
  private Button menuButton(String labelKey, EventHandler<MouseEvent> onClickHandler) {
    Button button = new StyledButton(this.serviceProvider, labelKey, onClickHandler);
    button.setMaxWidth(Double.MAX_VALUE);
    return button;
  }

  /**
   * Returns the {@link View}'s managed {@link Pane}. Stricter requirement than that of the
   * overridden method in {@link Renderable}, which guarantees only a {@link javafx.scene.Node}.
   *
   * @return the {@link View}'s managed {@link Pane}.
   */
  @Override
  public Pane getRenderingNode() {
    return this.primaryView;
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
        .add(serviceProvider.themeService().getTheme().getStylesheet());
  }
}
