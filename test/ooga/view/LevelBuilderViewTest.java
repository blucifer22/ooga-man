package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ooga.model.leveldescription.LevelBuilder;
import ooga.model.leveldescription.LevelBuilder.BuilderState;
import ooga.view.audio.AudioService;
import ooga.view.audio.ThemedAudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.ServiceProvider;
import ooga.view.views.sceneroots.LevelBuilderView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LevelBuilderViewTest extends CustomApplicationTest {
  private static class TestHarness implements ViewStackService {
    private int unwinds;

    @Override
    public void unwind() {
      unwinds++;
    }
  }

  private Stage stage;
  private LevelBuilderView view;
  private TestHarness testHarness;
  private LevelBuilder lb;

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.stage = stage;
    stage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      this.testHarness = new TestHarness();
      ExceptionService es = new GraphicalExceptionService();
      ThemeService ts = new SerializedThemeService(es);
      AudioService as = new ThemedAudioService(ts, es);
      lb = new LevelBuilder();
      view = new LevelBuilderView(new ServiceProvider(es, as, ts, new BundledLanguageService(es),
          testHarness), lb);
      stage.setScene(new Scene(view.getRenderingNode(), 600, 600));
    });
  }

  @Test
  public void testUnwind() throws InterruptedException {
    syncFXRun(() -> {
      Button unwind = lookup("#button-mainMenu").query();
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {}
      moveTo(unwind);
      unwind.getOnMouseClicked().handle(null);
      assertEquals(1, testHarness.unwinds);
    });
  }

  @Test
  public void testNext() throws InterruptedException {
    syncFXRun(() -> {
      Button next = lookup("#button-next").query();
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {}
      moveTo(next);
      next.getOnMouseClicked().handle(null);
      assertEquals(lb.getBuilderState(), BuilderState.SPRITE_PLACEMENT);
    });
  }
}
