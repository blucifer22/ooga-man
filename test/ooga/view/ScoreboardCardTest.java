package ooga.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ooga.model.ImmutablePlayer;
import ooga.model.api.GameStateObservable;
import ooga.view.audio.AudioService;
import ooga.view.audio.ThemedAudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.views.components.scenecomponents.ScoreboardCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScoreboardCardTest extends CustomApplicationTest {
  private static final int DEFAULT_SIZE = 200;
  private StackPane primaryView = new StackPane();
  private TestHarness harness;
  private ScoreboardCard card;

  private static class TestHarness implements GameStateObservable {
    private static class HarnessPlayer implements ImmutablePlayer {
      private int score;
      private int wins;
      private int id;

      @Override
      public int getScore() {
        return score;
      }

      @Override
      public int getRoundWins() {
        return wins;
      }

      @Override
      public int getID() {
        return id;
      }

      public void set(int score, int wins, int id) {
        this.score = score;
        this.wins = wins;
        this.id = id;
      }
    }

    private int roundNum;
    private int lives;
    private final List<ImmutablePlayer> players;

    public TestHarness() {
      this.players = new ArrayList<>();
      HarnessPlayer p1 = new HarnessPlayer();
      p1.set(0, 0, 1);
      HarnessPlayer p2 = new HarnessPlayer();
      p2.set(0,0, 2);
      players.add(p1);
      players.add(p2);
    }

    @Override
    public List<ImmutablePlayer> getPlayers() {
      return players;
    }

    @Override
    public int getPacmanLivesRemaining() {
      return lives;
    }

    @Override
    public int getRoundNumber() {
      return roundNum;
    }

    public void set(int round, int lives) {
      this.roundNum = round;
      this.lives = lives;
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    stage.setScene(new Scene(primaryView, DEFAULT_SIZE, DEFAULT_SIZE));
    stage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      primaryView.getChildren().clear();
      ExceptionService es = new GraphicalExceptionService();
      ThemeService ts = new SerializedThemeService(es);
      AudioService as = new ThemedAudioService(ts, es);
      ServiceProvider provider = new ServiceProvider(es, as, ts, new BundledLanguageService(es),
          () -> {});
      this.card = new ScoreboardCard(provider);
      primaryView.getChildren().add(this.card.getRenderingNode());

      // reset harness
      this.harness = new TestHarness();
    });
  }

  @Test
  public void initializationTest() throws InterruptedException {
    syncFXRun(() -> {
      card.onGameStateUpdate(harness);
    });
    Thread.sleep(2000);
  }

  @Test
  public void updateTest() throws InterruptedException {
    syncFXRun(() -> {
      card.onGameStateUpdate(harness);
    });
    Thread.sleep(500);
    syncFXRun(() -> {
      harness.lives = 500;
      harness.roundNum = 2;
      Label l = lookup("#label-scoreboard-lives").query();
      card.onGameStateUpdate(harness);
      assertTrue(l.getText().contains("500"));
    });
    Thread.sleep(2000);
  }
}
