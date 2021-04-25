package ooga.view.views.components;

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
import ooga.model.ImmutablePlayer;
import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;
import ooga.view.internal_api.Renderable;
import ooga.view.uiservice.UIServiceProvider;

public class ScoreboardCard implements GameStateObserver, Renderable {

  private static final int NUM_COLS = 3;
  private GameStateObservable dataSource;
  private final UIServiceProvider serviceProvider;
  private final GridPane view;
  private final TreeMap<Integer, PlayerDataBindingContainer> dataBindingContainers;
  private final SimpleIntegerProperty livesProperty;
  private final SimpleIntegerProperty roundProperty;
  private boolean initialized = false;

  public ScoreboardCard(UIServiceProvider serviceProvider) {
    this.view = new GridPane();
    this.serviceProvider = serviceProvider;
    this.dataBindingContainers = new TreeMap<>();
    this.livesProperty = new SimpleIntegerProperty();
    this.roundProperty = new SimpleIntegerProperty();
  }

  private void configureData() {
    for (ImmutablePlayer p : dataSource.getPlayers()) {
      dataBindingContainers.put(p.getID(), new PlayerDataBindingContainer(p));
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
  }

  private void setConstraints() {
    int numPlayers = this.dataSource.getPlayers().size();
    for (int i = 0; i < numPlayers; i++) {
      RowConstraints rc = new RowConstraints();
      rc.setPercentHeight(100.0 / (numPlayers + 4.0));
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

  private void populateData() {
    ReadOnlyStringProperty roundNum = stringFor("roundNumber");
    StringBinding roundBinding = Bindings.createStringBinding(() ->
        String.format(roundNum.get(), roundProperty.get()), roundNum, roundProperty);
    this.view.add(new StyledBoundLabel(roundBinding, "heading"), 0, 0, NUM_COLS, 1);

    ReadOnlyStringProperty livesRemaining = stringFor("livesRemaining");
    StringBinding livesBinding = Bindings.createStringBinding(() ->
        String.format(livesRemaining.get(), livesProperty.get()), livesRemaining, livesProperty);
    this.view.add(new StyledBoundLabel(livesBinding, "heading", "scoreboard-lives"),
        0, 1, NUM_COLS, 1);

    this.view.add(new Label(), 0, 2, NUM_COLS, 1);

    this.view.add(new StyledBoundLabel(stringFor("player"), "heading"), 0, 3);
    this.view.add(new StyledBoundLabel(stringFor("score"), "heading"), 1, 3);
    this.view.add(new StyledBoundLabel(stringFor("wins"), "heading"), 2, 3);

    for (ImmutablePlayer p : dataSource.getPlayers()) {
      int rowNum = p.getID() + 3;
      PlayerDataBindingContainer container = dataBindingContainers.get(p.getID());
      this.view.add(
          new StyledBoundLabel(new SimpleIntegerProperty(p.getID()).asString(), "body",
                  "scoreboard-player-"+p.getID()), 0, rowNum
      );
      this.view.add(
          new StyledBoundLabel(container.scoreProperty.asString(), "body",
              "scoreboard-score-"+p.getID()), 1, rowNum
      );
      this.view.add(
          new StyledBoundLabel(container.winProperty.asString(), "body",
              "scoreboard-score-"+p.getID()), 2, rowNum
      );
    }
  }

  private void refresh() {
    for (ImmutablePlayer p : dataSource.getPlayers()) {
      dataBindingContainers.get(p.getID()).update(p);
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
  }

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

  @Override
  public Node getRenderingNode() {
    return this.view;
  }

  private static class PlayerDataBindingContainer {

    private final SimpleIntegerProperty scoreProperty;
    private final SimpleIntegerProperty winProperty;

    public PlayerDataBindingContainer(ImmutablePlayer p) {
      this.scoreProperty = new SimpleIntegerProperty(p.getScore());
      this.winProperty = new SimpleIntegerProperty(p.getRoundWins());
    }

    public void update(ImmutablePlayer p) {
      scoreProperty.set(p.getScore());
      winProperty.set(p.getRoundWins());
    }
  }
}
