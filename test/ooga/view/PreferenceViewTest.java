package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ooga.view.internal_api.PreferenceResponder;
import ooga.view.internal_api.ViewStackManager;
import ooga.view.language.api.LanguageService;
import ooga.view.language.bundled.BundledLanguageService;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.PreferenceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreferenceViewTest extends CustomApplicationTest {

  private class TestHarness implements PreferenceResponder, ThemeService, LanguageService,
      ViewStackManager {
    private BundledLanguageService ls = new BundledLanguageService();
    private ThemeService ts = new SerializedThemeService();
    private final int[] state = new int[2];
    private String language = null;

    @Override
    public void setLanguage(String language) {
      ls.setLanguage(language);
    }

    @Override
    public ReadOnlyStringProperty getLocalizedString(String s) {
      return ls.getLocalizedString(s);
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
      this.primaryStage.setScene(new Scene(new PreferenceView(harness, harness, harness, harness).getRenderingNode(), 600, 600));
    });
  }

  @Test
  public void testUnwind() throws InterruptedException {
    Thread.sleep(500);

    Node backButton = lookup("#menu-button-previousMenu").query();
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

    ComboBox<Pair<String, String>> cbox = lookup("#menu-combo-lang-select").query();
    syncFXRun(() -> {
      moveTo(cbox);
      cbox.getSelectionModel().select(0);
      cbox.getSelectionModel().getSelectedIndex();

      while (!cbox.getItems().get(cbox.getSelectionModel().getSelectedIndex()).toString().equals(
          "English")) {
        cbox.getSelectionModel().selectNext();
      }
    });

    assertEquals("Language", harness.getLocalizedString("language").get());

    Thread.sleep(500);

    syncFXRun(() -> {
      moveTo(cbox);
      cbox.getSelectionModel().select(0);
      cbox.getSelectionModel().getSelectedIndex();

      while (!cbox.getItems().get(cbox.getSelectionModel().getSelectedIndex()).toString().equals(
          "Español")) {
        cbox.getSelectionModel().selectNext();
      }
    });

    assertEquals("Idioma", harness.getLocalizedString("language").get());

    Thread.sleep(500);
  }
}
