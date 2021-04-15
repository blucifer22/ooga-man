package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.view.internal_api.MainMenuResponder;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.MenuView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainMenuTest extends CustomApplicationTest {

  private class TestHarness implements MainMenuResponder {
    private final int [] state = new int[3];

    @Override
    public void startGame() {
      state[0] += 1;
      displayMessageInScene("Game started!");
    }

    @Override
    public void openLevelBuilder() {
      state[1] += 1;
      displayMessageInScene("Level builder opened!");
    }

    @Override
    public void openPreferences() {
      state[2] += 1;
      displayMessageInScene("Preferences opened!");
    }

    public int [] getState() {
      return state;
    }

    private void displayMessageInScene(String s) {
      Text t = new Text(s);
      Scene sc = new Scene(new StackPane(t), 600, 600);
      primaryStage.setScene(sc);
    }
  }

  private Stage primaryStage;
  private MenuView mainMenu;
  private TestHarness testHarness;

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
      this.mainMenu = new MenuView(
          this.testHarness,
          new SerializedThemeService(),
          new BundledLanguageService());
      this.primaryStage.setScene(new Scene(mainMenu.getRenderingNode(), 600, 600));
    });
  }

  @Test
  public void startGameTest() throws InterruptedException {
    test("startGame", 0);
  }

  @Test
  public void levelBuilderTest() throws InterruptedException {
    test("openLevelBuilder", 1);
  }

  @Test
  public void openPreferencesTest() throws InterruptedException {
    test("openPreferences", 2);
  }

  private void test(String testButtonID, int testHarnessIndex) throws InterruptedException {
    Thread.sleep(500);

    HashMap<String, Node> nodes = buttons();
    Node target = nodes.get(testButtonID);
    syncFXRun(() -> {
      moveTo(target);
      target.getOnMouseClicked().handle(null);
    });

    assertEquals(1, testHarness.getState()[testHarnessIndex]);

    Thread.sleep(500);
  }

  private HashMap<String, Node> buttons() {
    HashMap<String, Node> nodes = new HashMap<>();
    for (String s: new String[] {"startGame", "openLevelBuilder", "openPreferences"}) {
      nodes.put(s, lookup("#menu-button-"+s).query());
    }
    return nodes;
  }
}
