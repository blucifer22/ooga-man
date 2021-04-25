package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ooga.view.audio.AudioService;
import ooga.view.audio.ThemedAudioService;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.internal_api.ViewStackService;
import ooga.view.language.api.LanguageSelectionService;
import ooga.view.language.api.LanguageService;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeSelectionService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.uiservice.UIPreferenceService;
import ooga.view.uiservice.UIServiceProvider;
import ooga.view.views.sceneroots.PreferenceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreferenceViewTest extends CustomApplicationTest {

  private class TestHarness extends BundledLanguageService implements ThemeService,
      LanguageService, ViewStackService, UIServiceProvider, UIPreferenceService {
    private SerializedThemeService ts = new SerializedThemeService(new GraphicalExceptionService());
    private AudioService as = new ThemedAudioService(ts, new GraphicalExceptionService());
    private final int[] state = new int[2];
    private String language = null;

    public TestHarness() {
      super(new GraphicalExceptionService());
    }

    @Override
    public void setLanguage(String language) {
      super.setLanguage(language);
      this.language = language;
    }

    @Override
    public Theme getTheme() {
      return ts.getTheme();
    }

    @Override
    public void addThemedObject(ThemedObject themedObject) {
      ts.addThemedObject(themedObject);
    }

    @Override
    public void unwind() {
      primaryStage.setScene(new Scene(new StackPane(new Text("Exited menu.")), 600, 600));
      state[0] += 1;
    }

    public int[] getState() {
      return this.state;
    }

    @Override
    public AudioService audioService() {
      return as;
    }

    @Override
    public ExceptionService exceptionService() {
      return new GraphicalExceptionService();
    }

    @Override
    public ThemeService themeService() {
      return this;
    }

    @Override
    public LanguageService languageService() {
      return this;
    }

    @Override
    public ViewStackService viewStackManager() {
      return this;
    }

    @Override
    public LanguageSelectionService languageSelectionService() {
      return this;
    }

    @Override
    public ThemeSelectionService themeSelectionService() {
      return this.ts;
    }
  }

  private Stage primaryStage;
  private TestHarness harness;

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.primaryStage = stage;
    this.primaryStage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      this.harness = new TestHarness();
      this.primaryStage.setScene(new Scene(new PreferenceView(this.harness, this.harness).getRenderingNode(), 600,
          600));
    });
  }

  @Test
  public void testUnwind() throws InterruptedException {
    Thread.sleep(500);

    Node backButton = lookup("#button-previousMenu").query();
    syncFXRun(() -> {
      moveTo(backButton);
      backButton.getOnMouseClicked().handle(null);
    });

    assertEquals(1, harness.getState()[0]);

    Thread.sleep(500);
  }

  @Test
  public void testLangChange() throws InterruptedException {
    Thread.sleep(500);

    ComboBox<Pair<String, String>> cbox = lookup("#language-select-dropdown").query();
    syncFXRun(() -> {
      moveTo(cbox);
      cbox.getSelectionModel().select(0);
      cbox.getSelectionModel().getSelectedIndex();

      while (!cbox.getItems().get(cbox.getSelectionModel().getSelectedIndex()).getKey().equals(
          "english")) {
        cbox.getSelectionModel().selectNext();
      }
    });

    assertEquals("Language", harness.getLocalizedString("language").get());

    Thread.sleep(500);

    syncFXRun(() -> {
      moveTo(cbox);
      cbox.getSelectionModel().select(0);

      while (!cbox.getItems().get(cbox.getSelectionModel().getSelectedIndex()).getKey().equals(
          "spanish")) {
        System.out.println(cbox.getItems().get(cbox.getSelectionModel().getSelectedIndex()).toString());
        cbox.getSelectionModel().selectNext();
      }
    });

    assertEquals("Idioma", harness.getLocalizedString("language").get());

    Thread.sleep(500);
  }
}
