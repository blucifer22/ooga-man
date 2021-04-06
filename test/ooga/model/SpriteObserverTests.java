package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteEvent.EventType;
import ooga.model.api.SpriteObserver;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteObserverTests {

  public static final double FRAME_RATE = 1.0 / 60;
  private TestObservableSprite observable;
  private TestObserver observer;

  @BeforeEach
  public void setUpObserver() {
    observable = new TestObservableSprite();
    observer = new TestObserver();
    System.out.println(EventType.values());
  }

  @Test
  public void noObserversTest() {
    observable.step(FRAME_RATE, null);
    assertEquals(null, observer.getLastEvent());
    assertEquals(null, observer.getLastSender());
  }

  @Test
  public void simpleTranslationObserver() {
    observable.addObserver(observer, EventType.TRANSLATE);
    observable.step(FRAME_RATE, null);
    assertEquals(EventType.TRANSLATE, observer.getLastEvent());
    assertEquals(TestObservableSprite.SPRITE_TYPE, observer.getLastSender());
    assertEquals(new Vec2(0.5, 0), observer.getLastCoordinates().getPosition());
  }

  @Test
  public void noArgumentsTest() {
    // Subscribe to all events
    observable.addObserver(observer);
    observable.step(FRAME_RATE, null);
    assertEquals(EventType.TRANSLATE, observer.getLastEvent());
    assertEquals(TestObservableSprite.SPRITE_TYPE, observer.getLastSender());
    assertEquals(new Vec2(0.5, 0), observer.getLastCoordinates().getPosition());
  }
}

class TestObserver implements SpriteObserver {

  private EventType lastEventType;
  private String lastSender;
  private SpriteCoordinates lastCoordinates;

  @Override
  public void onSpriteUpdate(SpriteEvent e) {
//    System.out.println(e.getSender().getType());
//    System.out.println(e.getEventType());
    lastEventType = e.getEventType();
    lastSender = e.getSender().getType();
    lastCoordinates = e.getSender().getCenter();
  }

  public SpriteCoordinates getLastCoordinates() {
    return lastCoordinates;
  }

  public String getLastSender() {
    return lastSender;
  }

  public EventType getLastEvent() {
    return lastEventType;
  }
}

class TestObservableSprite extends Sprite {

  public static final String SPRITE_TYPE = "TEST_SPRITE";

  @Override
  public String getType() {
    return SPRITE_TYPE;
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public void step(double dt, PacmanGrid grid) {
    getCoordinates().setPosition(new Vec2(0.5, 0));
    notifyObservers(EventType.TRANSLATE);
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
