package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ooga.view.audio.AudioService;
import ooga.view.audio.ThemedAudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.views.sceneroots.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameViewTest extends CustomApplicationTest {
  private Stage primaryStage;
  private GameView gameView;
  private TestHarness testHarness;

  private static class TestHarness implements ViewStackService {
    private int unwindCount;

    @Override
    public void unwind() {
      unwindCount++;
    }

    public int getUnwindCount() {
      return unwindCount;
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.primaryStage = stage;
    this.primaryStage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      this.testHarness = new TestHarness();
      ExceptionService es = new GraphicalExceptionService();
      ThemeService ts = new SerializedThemeService(es);
      AudioService as = new ThemedAudioService(ts, es);
      ServiceProvider provider = new ServiceProvider(es, as, ts, new BundledLanguageService(es), testHarness);
      this.gameView = new GameView(provider);
      this.primaryStage.setScene(new Scene(this.gameView.getRenderingNode(), 600, 600));
    });
  }

  @Test
  public void testUnwind() throws InterruptedException {
    Thread.sleep(500);
    assertEquals(0, testHarness.getUnwindCount());
    Button mainMenuButton = lookup("#button-mainMenu").queryButton();
    moveTo(mainMenuButton);
    syncFXRun(() -> {
      mainMenuButton.getOnMouseClicked().handle(null);
    });
    assertEquals(1, testHarness.getUnwindCount());
  }
}
