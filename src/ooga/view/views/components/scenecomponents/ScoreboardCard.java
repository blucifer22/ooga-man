package ooga.view.views.components.scenecomponents;

import java.util.TreeMap;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;
import ooga.model.api.ImmutablePlayer;
import ooga.view.internal_api.Renderable;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.components.reusable.StyledBoundLabel;

/**
 * A scoreboard "card" view component. Displays information received by observer callbacks via the
 * {@link GameStateObserver} interface. Implements {@link Renderable} and may thus be rendered as a
 * portion of, but not as an entire, scene.
 * <p>
 * The {@link ScoreboardCard} expects to be added as a {@link GameStateObserver} to a {@link
 * GameStateObservable}. It will <em>not</em> query an {@link GameStateObservable} unprompted.
 *
 * @author David Coffman
 */
public class ScoreboardCard implements GameStateObserver, Renderable {

  private static final int NUM_COLS = 3;
  private static final int NUM_AUX_ROWS = 4;
  private final UIServiceProvider serviceProvider;
  private final GridPane view;
  private final TreeMap<Integer, PlayerDataBindingContainer> dataBindingContainers;
  private final SimpleIntegerProperty livesProperty;
  private final SimpleIntegerProperty roundProperty;
  private GameStateObservable dataSource;
  private boolean initialized = false;

  /**
   * Sole constructor for {@link ScoreboardCard}.
   *
   * @param serviceProvider a {@link UIServiceProvider} to provide UI services as desired
   */
  public ScoreboardCard(UIServiceProvider serviceProvider) {
    this.view = new GridPane();
    this.serviceProvider = serviceProvider;
    this.dataBindingContainers = new TreeMap<>();
    this.livesProperty = new SimpleIntegerProperty();
    this.roundProperty = new SimpleIntegerProperty();
  }

  // Initial loading of player data into map
  private void configureData() {
    for (ImmutablePlayer p : dataSource.getPlayers()) {
      dataBindingContainers.put(p.getID(), new PlayerDataBindingContainer(p));
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
  }

  // Configure the GridPane constraints for the scoreboard card's primary view element.
  private void setConstraints() {
    int numPlayers = this.dataSource.getPlayers().size();
    for (int i = 0; i < numPlayers; i++) {
      RowConstraints rc = new RowConstraints();
      rc.setPercentHeight(100.0 / (numPlayers + NUM_AUX_ROWS));
      this.view.getRowConstraints().add(rc);
    }
    for (int i = 0; i < NUM_COLS; i++) {
      ColumnConstraints cc = new ColumnConstraints();
      cc.setPercentWidth(100.0 / (NUM_COLS));
      this.view.getColumnConstraints().add(cc);
    }
    this.view.setHgap(5);
    this.view.getStyleClass().add("scoreboard");
  }

  // Initial data loading; after initial loading, all visual updates are accomplished by property
  // binding.
  private void populateData() {
    ReadOnlyStringProperty roundNum = stringFor("roundNumber");
    StringBinding roundBinding =
        Bindings.createStringBinding(
            () -> String.format(roundNum.get(), roundProperty.get()), roundNum, roundProperty);
    this.view.add(new StyledBoundLabel(roundBinding, "heading"), 0, 0, NUM_COLS, 1);

    ReadOnlyStringProperty livesRemaining = stringFor("livesRemaining");
    StringBinding livesBinding =
        Bindings.createStringBinding(
            () -> String.format(livesRemaining.get(), livesProperty.get()),
            livesRemaining,
            livesProperty);
    this.view.add(
        new StyledBoundLabel(livesBinding, "heading", "scoreboard-lives"), 0, 1, NUM_COLS, 1);

    this.view.add(new Label(), 0, 2, NUM_COLS, 1);

    this.view.add(new StyledBoundLabel(stringFor("player"), "heading"), 0, 3);
    this.view.add(new StyledBoundLabel(stringFor("score"), "heading"), 1, 3);
    this.view.add(new StyledBoundLabel(stringFor("wins"), "heading"), 2, 3);

    for (ImmutablePlayer p : dataSource.getPlayers()) {
      int rowNum = p.getID() + 3;
      PlayerDataBindingContainer container = dataBindingContainers.get(p.getID());
      this.view.add(
          new StyledBoundLabel(
              new SimpleIntegerProperty(p.getID()).asString(),
              "body",
              "scoreboard-player-" + p.getID()),
          0,
          rowNum);
      this.view.add(
          new StyledBoundLabel(
              container.scoreProperty.asString(), "body", "scoreboard-score-" + p.getID()),
          1,
          rowNum);
      this.view.add(
          new StyledBoundLabel(
              container.winProperty.asString(), "body", "scoreboard-score-" + p.getID()),
          2,
          rowNum);
    }
  }

  // Refreshes property bindings. Sole code called on an observer callback.
  private void refresh() {
    for (ImmutablePlayer p : dataSource.getPlayers()) {
      dataBindingContainers.get(p.getID()).update(p);
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
  }

  // utility method to reduce code duplication; retrieves binding for a given keyed string
  private ReadOnlyStringProperty stringFor(String key) {
    return this.serviceProvider.languageService().getLocalizedString(key);
  }

  /**
   * Called when a game state change occurs, such as when the score or pacman lives remaining
   * changes
   *
   * @param state contains information about the Pac-Man game state
   */
  @Override
  public void onGameStateUpdate(GameStateObservable state) {
    this.dataSource = state;
    if (!initialized) {
      configureData();
      setConstraints();
      populateData();
      initialized = true;
    } else {
      refresh();
    }
  }

  /**
   * Returns the {@link ScoreboardCard}'s managed {@link Node}.
   *
   * @return the {@link ScoreboardCard}'s managed {@link Node}.
   */
  @Override
  public Node getRenderingNode() {
    return this.view;
  }

  /**
   * Inner class used for player data binding management.
   *
   * @author David Coffman
   */
  private static class PlayerDataBindingContainer {

    private final SimpleIntegerProperty scoreProperty;
    private final SimpleIntegerProperty winProperty;

    /**
     * Instantiates a {@link PlayerDataBindingContainer} from an {@link ImmutablePlayer}.
     *
     * @param p the {@link ImmutablePlayer} to bind
     */
    public PlayerDataBindingContainer(ImmutablePlayer p) {
      this.scoreProperty = new SimpleIntegerProperty(p.getScore());
      this.winProperty = new SimpleIntegerProperty(p.getRoundWins());
    }

    /**
     * Called during observer callbacks to {@link ScoreboardCard#onGameStateUpdate(GameStateObservable)}.
     *
     * @param p the {@link ImmutablePlayer} to update
     */
    public void update(ImmutablePlayer p) {
      scoreProperty.set(p.getScore());
      winProperty.set(p.getRoundWins());
    }
  }
}
