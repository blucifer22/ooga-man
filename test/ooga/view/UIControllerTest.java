package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ooga.controller.GameStateController;
import ooga.model.api.GameStateObservationComposite;
import ooga.view.io.HumanInputConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UIControllerTest extends CustomApplicationTest {
  private static class TestHarness implements HumanInputConsumer, GameStateController {
    private int gameStartCount;
    private int gameStopCount;

    @Override
    public void startGame(GameStateObservationComposite rootObserver) {
      gameStartCount++;
    }

    @Override
    public void pauseGame() {
      gameStopCount++;
    }

    /**
     * This method handles the behavior associated with depressed keys and cues the calling class to
     * spin up an appropriate response.
     *
     * @param keyCode The KeyCode of the currently depressed key.
     */
    @Override
    public void onKeyPress(KeyCode keyCode) {
    }

    /**
     * This method handles the behavior associated with the release of a key
     *
     * @param keyCode The KeyCode of the key that is being released.
     */
    @Override
    public void onKeyRelease(KeyCode keyCode) {
    }
  }

  private Stage stage;
  private TestHarness harness;
  private UIController controller;

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.stage = stage;
    stage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      this.harness = new TestHarness();
      this.controller = new UIController(stage, harness, harness);
    });
  }

  @Test
  public void testStart() throws InterruptedException {
    syncFXRun(() -> {
      controller.startGame();
      assertEquals(1, harness.gameStartCount);
      controller.unwind();
      assertEquals(1, harness.gameStartCount);
      controller.startGame();
      assertEquals(2, harness.gameStartCount);
    });
    Thread.sleep(500);
  }

  @Test
  public void testOpenViews() throws InterruptedException {
    syncFXRun(() -> {
      controller.openLevelBuilder();
      try {
        lookup("#level-builder-panel").query();
      } catch (Exception e) {
        fail();
      }
      controller.openPreferences();
      try {
        lookup("#label-language-select-label").query();
      } catch (Exception e) {
        fail();
      }
    });
  }
}
