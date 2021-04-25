package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.language.bundled.BundledLanguageService;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class LanguageServiceTest extends ApplicationTest {

  private Text label;
  private BundledLanguageService bls;
  private static final String BUNDLE_ROOT = "resources.languages/";

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.label = new Text();
    ExceptionService es = new GraphicalExceptionService();
    this.bls = new BundledLanguageService(es);
    this.label.textProperty().bind(bls.getLocalizedString("pacman"));
    stage.setScene(new Scene(new StackPane(label), 400, 400));
    stage.show();
  }

  @Test
  public void simpleTest() {
    this.bls.setLanguage("english");
    assertEquals(bundleFor("english").getString("pacman"), this.label.getText());
  }

  @Test
  public void switchTest() {
    Platform.runLater(() -> {
      String[] langs = new String[]{"english", "spanish"};

      for (String lang : langs) {
        this.bls.setLanguage(lang);
        assertEquals(bundleFor(lang).getString("pacman"), this.label.getText());
      }
    });
  }

  private ResourceBundle bundleFor(String language) {
    return ResourceBundle.getBundle(BUNDLE_ROOT+language);
  }
}
