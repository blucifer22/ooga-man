package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ooga.view.audio.ThemedAudioService;
import ooga.view.internal_api.ViewStackManager;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.views.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameViewTest extends CustomApplicationTest {
  private Stage primaryStage;
  private GameView gameView;
  private TestHarness testHarness;

  private static class TestHarness implements ViewStackManager {
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
      ThemeService ts = new SerializedThemeService();
      ServiceProvider provider = new ServiceProvider(new ThemedAudioService(ts), ts,
          new BundledLanguageService(), testHarness);
      this.gameView = new GameView(provider);
      this.primaryStage.setScene(new Scene(this.gameView.getRenderingNode(), 600, 600));
    });
  }

  @Test
  public void testUnwind() throws InterruptedException {
    Thread.sleep(500);
    assertEquals(0, testHarness.getUnwindCount());
    Button mainMenuButton = lookup("#gameview-main-menu-button").queryButton();
    moveTo(mainMenuButton);
    mainMenuButton.getOnMouseClicked().handle(null);
    assertEquals(1, testHarness.getUnwindCount());
  }
}
