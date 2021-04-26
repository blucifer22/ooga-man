package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ooga.model.SpriteCoordinates;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteEvent.EventType;
import ooga.model.api.SpriteObserver;
import ooga.util.Vec2;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.GraphicalExceptionService;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.serialized.SerializedThemeService;
import ooga.view.views.components.scenecomponents.SpriteView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteViewTest extends CustomApplicationTest {
  private Group graphicsGroup;
  private SpriteView sv;
  private Rectangle spriteViewNode;
  private ThemeService ts;
  private DoubleProperty size;
  private SpriteHarness harness;

  private static class SpriteHarness implements ObservableSprite {

    private SpriteCoordinates sc;
    private Vec2 angle;
    private boolean visible;

    public HashMap<EventType, Set<SpriteObserver>> observers;

    public SpriteHarness() {
      this.sc = new SpriteCoordinates();
      this.observers = new HashMap<>();
      for (EventType e: EventType.values()) {
        observers.put(e, new HashSet<>());
      }
      this.angle = new Vec2(1, 0);
      this.visible = true;
    }

    public void setCoords(double x, double y) {
      this.sc = new SpriteCoordinates(new Vec2(x, y));
      for (SpriteObserver observer : observers.get(EventType.TRANSLATE)) {
        observer.onSpriteUpdate(new SpriteEvent(this, EventType.TRANSLATE));
      }
    }

    public void setDirection(Vec2 angle) {
      this.angle = angle;
      for (SpriteObserver observer : observers.get(EventType.ROTATE)) {
        observer.onSpriteUpdate(new SpriteEvent(this, EventType.ROTATE));
      }
    }

    public void setVisible(boolean visible) {
      this.visible = visible;
      for (SpriteObserver observer : observers.get(EventType.VISIBILITY)) {
        observer.onSpriteUpdate(new SpriteEvent(this, EventType.TRANSLATE));
      }
    }

    @Override
    public String getCostume() {
      return "blank_1";
    }

    @Override
    public SpriteCoordinates getCoordinates() {
      return this.sc;
    }

    @Override
    public Vec2 getDirection() {
      return this.angle;
    }

    @Override
    public boolean isVisible() {
      return visible;
    }

    @Override
    public void addObserver(SpriteObserver so, EventType... observedEvents) {
      SpriteEvent.EventType[] eventsToRegister =
          observedEvents.length == 0 ? SpriteEvent.EventType.values() : observedEvents;
      for (SpriteEvent.EventType observedEvent : eventsToRegister) {
        observers.get(observedEvent).add(so);
      }
    }

    @Override
    public void removeObserver(SpriteObserver so) {
      for (Set<SpriteObserver> s: observers.values()) {
        s.remove(so);
      }
    }
  }

  @BeforeEach
  public void reset() throws InterruptedException {
    syncFXRun(() -> {
      ExceptionService es = new GraphicalExceptionService();
      ts = new SerializedThemeService(es);
      size = new SimpleDoubleProperty(50);
      harness = new SpriteHarness();
      sv = new SpriteView(harness, ts, size);
      graphicsGroup.getChildren().add(sv.getRenderingNode());
      this.spriteViewNode = (Rectangle) sv.getRenderingNode();
      harness.setCoords(4, 4);
    });
  }

  @AfterEach
  public void clearGraphics() throws InterruptedException {
    syncFXRun(() -> {
      this.graphicsGroup.getChildren().clear();
    });
  }

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.graphicsGroup = new Group();

    stage.setScene(new Scene(this.graphicsGroup, 400, 400));
    stage.show();
  }

  @Test
  public void simpleDimensionTest() throws InterruptedException {
    double TILE_SIZE = 100;

    syncFXRun(() -> {
      size.setValue(TILE_SIZE);
    });

    Thread.sleep(100);

    syncFXRun(() -> {
      assertTrue(this.spriteViewNode.isVisible());
      assertEquals(100, this.spriteViewNode.getWidth());
      assertEquals(100, this.spriteViewNode.getHeight());
    });

    Thread.sleep(100);
  }

  @Test
  public void simpleTranslationTest() throws InterruptedException {
    double TILE_SIZE = 50;

    for (int i = 0; i < 5; i++) {
      int finalI = i;
      syncFXRun(() -> {
        size.setValue(TILE_SIZE);
        harness.setCoords(finalI, finalI);
      });

      Thread.sleep(100);

      syncFXRun(() -> {
        assertEquals(finalI*TILE_SIZE - TILE_SIZE/2, this.spriteViewNode.getTranslateX(), 0.005);
        assertEquals(finalI*TILE_SIZE - TILE_SIZE/2, this.spriteViewNode.getTranslateY(), 0.005);
      });

      Thread.sleep(100);
    }
  }

  @Test
  public void simpleRotationTest() throws InterruptedException {
    for (int i = 0; i < 5; i++) {
      int finalI = i;
      syncFXRun(() -> {
        harness.setDirection(new Vec2(Math.cos(finalI), Math.sin(finalI)));
      });

      Thread.sleep(100);

      syncFXRun(() -> {
        assertEquals(finalI*180/Math.PI, (360 + this.spriteViewNode.getRotate()) % 360, 0.005);
      });

      Thread.sleep(100);
    }
  }
}
