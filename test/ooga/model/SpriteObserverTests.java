package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteEvent.EventType;
import ooga.model.api.SpriteObserver;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.model.sprites.animation.StillAnimation;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpriteObserverTests {

  public static final double FRAME_RATE = 1.0 / 60;
  private TestObservableSprite observable;
  private TestObserver observer;
  private PacmanGameState state;

  @BeforeEach
  public void setUpObserver() {
    observable = new TestObservableSprite();
    observer = new TestObserver();
    System.out.println(EventType.values());
  }

  @Test
  public void noObserversTest() {
    observable.step(FRAME_RATE, state);
    assertEquals(null, observer.getLastEvent());
    assertEquals(null, observer.getLastSender());
  }

  @Test
  public void simpleTranslationObserver() {
    observable.addObserver(observer, EventType.TRANSLATE);
    observable.step(FRAME_RATE, state);
    assertEquals(EventType.TRANSLATE, observer.getLastEvent());
    assertEquals(TestObservableSprite.SPRITE_TYPE, observer.getLastSender());
    assertEquals(new Vec2(0.5, 0), observer.getLastCoordinates().getPosition());
  }

  @Test
  public void noArgumentsTest() {
    // Subscribe to all events
    observable.addObserver(observer);
    observable.step(FRAME_RATE, state);
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
//    System.out.println(e.getSender().getCostume());
//    System.out.println(e.getEventType());
    lastEventType = e.getEventType();
    lastSender = e.getSender().getCostume();
    lastCoordinates = e.getSender().getCoordinates();
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

  public static final String SPRITE_TYPE = "blank_1";

  public TestObservableSprite() {
    super("", SpriteAnimationFactory.SpriteAnimationType.BLANK);
  }

  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {

  }

  @Override
  public void step(double dt, MutableGameState pacmanGameState) {
    setPosition(new Vec2(0.5, 0));
    notifyObservers(EventType.TRANSLATE);
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }

  @Override
  public boolean isDeadlyToPacMan() {
    return false;
  }

  @Override
  public boolean eatsGhosts() {
    return false;
  }

  @Override
  public boolean isConsumable() {
    return false;
  }

  @Override
  public boolean isRespawnTarget() { return false; }

  @Override
  public boolean hasMultiplicativeScoring() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {

  }
}
