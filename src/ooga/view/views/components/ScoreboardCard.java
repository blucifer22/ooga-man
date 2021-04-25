package ooga.view.views.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    public ReadOnlyIntegerProperty scoreProperty() {
      return this.scoreProperty;
    }

    public ReadOnlyIntegerProperty winProperty() {
      return this.winProperty;
    }
  }

  private GameStateObservable dataSource;
  private final UIServiceProvider serviceProvider;
  private final GridPane view;
  private final TreeMap<Integer, PlayerDataBindingContainer> dataBindingContainers;
  private final SimpleIntegerProperty livesProperty;
  private final SimpleIntegerProperty roundProperty;
  private static final int NUM_COLS = 3;
  private boolean initialized = false;

  public ScoreboardCard(UIServiceProvider serviceProvider) {
    this.view = new GridPane();
    this.serviceProvider = serviceProvider;
    this.dataBindingContainers = new TreeMap<>();
    this.livesProperty = new SimpleIntegerProperty();
    this.roundProperty = new SimpleIntegerProperty();
  }

  private void configureData() {
    for (ImmutablePlayer p: dataSource.getPlayers()) {
      dataBindingContainers.put(p.getID(), new PlayerDataBindingContainer(p));
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
  }

  private void setConstraints() {
    int numPlayers = this.dataSource.getPlayers().size();
    for (int i = 0; i < numPlayers; i++) {
      RowConstraints rc = new RowConstraints();
      rc.setPercentHeight(100.0/(numPlayers+1.0));
      this.view.getRowConstraints().add(rc);
    }
    for (int i = 0; i < NUM_COLS; i++) {
      ColumnConstraints cc = new ColumnConstraints();
      cc.setPercentWidth(100.0/(NUM_COLS));
      this.view.getColumnConstraints().add(cc);
    }
  }

  private void populateData() {
    this.view.add(new Label("PLAYER"), 0, 0);
    this.view.add(new Label("SCORE"), 1, 0);
    this.view.add(new Label("WINS"), 2, 0);

    for (ImmutablePlayer p: dataSource.getPlayers()) {
      int rowNum = p.getID();
      PlayerDataBindingContainer container = dataBindingContainers.get(p.getID());
      this.view.add(new Label(String.format("%d", p.getID())), 0, rowNum);

      Label score = new Label();
      score.textProperty().bind(container.scoreProperty().asString("%d"));
      this.view.add(score, 1, rowNum);

      Label roundWins = new Label();
      roundWins.textProperty().bind(container.winProperty().asString("%d"));
      this.view.add(roundWins, 2, rowNum);
    }

    Label livesRemaining = new Label();
    livesRemaining.textProperty().bind(livesProperty.asString("%d LIVES REMAINING"));
    this.view.add(livesRemaining, 0, dataSource.getPlayers().size()+1, NUM_COLS, 1);
  }

  private void refresh() {
    for (ImmutablePlayer p: dataSource.getPlayers()) {
      dataBindingContainers.get(p.getID()).update(p);
    }
    livesProperty.setValue(dataSource.getPacmanLivesRemaining());
    roundProperty.setValue(dataSource.getRoundNumber());
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
}
