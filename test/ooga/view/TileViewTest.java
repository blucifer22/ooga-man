package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.HashSet;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ooga.model.TileCoordinates;
import ooga.model.api.ObservableTile;
import ooga.model.api.TileEvent;
import ooga.model.api.TileEvent.EventType;
import ooga.model.api.TileObserver;
import ooga.util.Vec2;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.components.TileView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TileViewTest extends CustomApplicationTest {
  private ThemeService themeService;
  private TileView tileView;
  private Rectangle tileViewRect;
  private SimpleDoubleProperty size;
  private TestHarness testHarness;
  private Stage primaryStage;

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.primaryStage = stage;
    this.primaryStage.show();
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      ExceptionService es = new GraphicalExceptionService();
      this.themeService = new SerializedThemeService(es);
      this.size = new SimpleDoubleProperty(50);
      this.testHarness = new TestHarness();
      this.tileView = new TileView(testHarness, size, themeService);

      try {
        Field f = TileView.class.getDeclaredField("tileRect");
        f.setAccessible(true);
        this.tileViewRect = (Rectangle) f.get(this.tileView);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }

      this.primaryStage.setScene(new Scene(new Group(tileView.getRenderingNode()), 600, 600));
    });
  }

  @Test
  public void initTest() throws InterruptedException {
    Thread.sleep(500);
    assertEquals(this.size.get(), this.tileViewRect.getLayoutX());
    assertEquals(this.size.get(), this.tileViewRect.getLayoutY());
    assertEquals(this.size.get(), this.tileViewRect.getWidth());
    assertEquals(this.size.get(), this.tileViewRect.getHeight());

  }

  @Test
  public void resizeTest() throws InterruptedException {
    Thread.sleep(500);
    this.size.set(100);
    Thread.sleep(500);
    assertEquals(this.size.get(), this.tileViewRect.getLayoutX());
    assertEquals(this.size.get(), this.tileViewRect.getLayoutY());
    assertEquals(this.size.get(), this.tileViewRect.getWidth());
    assertEquals(this.size.get(), this.tileViewRect.getHeight());
  }

  private static class TestHarness implements ObservableTile {
    private final HashSet<TileObserver> observers;
    private final double x = 1.5;
    private final double y = 1.5;
    private String type;

    public TestHarness() {
      this.observers = new HashSet<>();
      this.type = "test-using-nonexistent-resource";
    }

    @Override
    public TileCoordinates getCoordinates() {
      return new TileCoordinates(new Vec2(x, y));
    }

    @Override
    public String getType() {
      return type;
    }

    @Override
    public void addTileObserver(TileObserver observer, EventType... events) {
      this.observers.add(observer);
    }

    public void setType(String s) {
      this.type = s;
    }

    private void notifyAll(EventType type) {
      for (TileObserver observer: observers) {
        observer.onTileEvent(new TileEvent(this, type));
      }
    }
  }
}
