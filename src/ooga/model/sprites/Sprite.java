package ooga.model.sprites;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ooga.model.*;
import ooga.model.api.ObservableSprite;
import ooga.model.api.PowerupEventObserver;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteObserver;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.AnimationObserver;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

import static ooga.model.api.SpriteEvent.EventType.*;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements ObservableSprite, PowerupEventObserver, AnimationObserver {

  private final SpriteAnimationFactory animationFactory;
  private final SpriteCoordinates initialPosition;
  private final Vec2 initialDirection;
  private SwapClass swapClass;
  private InputSource inputSource;
  private Map<PacmanPowerupEvent, Runnable> powerupOptions = new HashMap<>();
  private InputSource defaultInputSource;
  private String inputString;
  private SpriteCoordinates position;
  private Vec2 direction;
  private Map<SpriteEvent.EventType, Set<SpriteObserver>> observers;
  private ObservableAnimation currentAnimation;
  private SpriteAnimationFactory.SpriteAnimationType currentAnimationType;

  /**
   * Initialize a Sprite
   *
   * @param spriteAnimationPrefix
   * @param startingAnimation
   * @param position
   * @param direction
   */
  protected Sprite(String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteCoordinates position,
      Vec2 direction) {
    this.position = position;
    this.direction = direction;
    this.animationFactory = new SpriteAnimationFactory(spriteAnimationPrefix);

    this.initialPosition = new SpriteCoordinates(position.getPosition());
    this.initialDirection = new Vec2(direction.getX(), direction.getY());
    initializeObserverMap();
    defaultInputSource = null;

    setCurrentAnimationType(startingAnimation);
  }

  protected Sprite(String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteDescription description) {
    this(spriteAnimationPrefix,
        startingAnimation,
        description.getCoordinates(),
        new Vec2(1, 0));
  }

  protected Sprite(String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation) {
    this(spriteAnimationPrefix,
        startingAnimation,
        new SpriteCoordinates(),
        new Vec2(1, 0));
  }

  /**
   * Sprites can change properties based on the current round.
   * @param roundNumber
   */
  public void adjustSpritePropertyWithLevel(int roundNumber) {
    // Does nothing (Overriden in specific child classes)
  }

  /**
   * This method resets the Sprite to its initial configuration at the start of a level.  This
   * method can be called to reset to the start of a new level.
   */
  public void reset() {
    position = initialPosition;
    direction = initialDirection;
  }

  private void initializeObserverMap() {
    observers = new HashMap<>();
    for (SpriteEvent.EventType eventType : SpriteEvent.EventType.values()) {
      observers.put(eventType, new HashSet<>());
    }
  }

  /**
   * Removes the Sprite from the game
   */
  public void delete(MutableGameState state) {
    state.prepareRemove(this);
  }

  /**
   * Returns the type of this Sprite.
   * <p>
   * Cannot be overridden -- type changes must go through setCostumeIndex().
   *
   * @return Current costume type, as a string, like "pacman_halfopen".
   */
  public final String getCostume() {
    return currentAnimation.getCurrentCostume();
  }

  public final ObservableAnimation getCurrentAnimation() {
    return currentAnimation;
  }

  private void setCurrentAnimation(ObservableAnimation newAnimation) {
    ObservableAnimation oldAnimation = currentAnimation;

    if (oldAnimation != null) {
      oldAnimation.removeObserver(this);
    }

    currentAnimation = newAnimation;
    notifyObservers(TYPE_CHANGE);

    newAnimation.addObserver(this);
  }

  protected SpriteAnimationFactory getAnimationFactory() {
    return animationFactory;
  }

  protected void setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType type) {
    if(type != currentAnimationType) {
      setCurrentAnimation(getAnimationFactory().createAnimation(type));
      currentAnimationType = type;
    }
  }

  @Override
  public void onCostumeChange(String newCostume) {
    notifyObservers(TYPE_CHANGE);
  }

  // coordinates of the tile above which this sprite's center lies

  /**
   * Coordinates of this Sprite. Also provides the tile coordinates.
   *
   * @return
   */
  public SpriteCoordinates getCoordinates() {
    return position;
  }

  /**
   * Translate this sprite to a new set of coordinates.
   * <p>
   * Notifies observers.
   *
   * @param c New position.
   */
  protected void setCoordinates(SpriteCoordinates c) {
    setPosition(c.getPosition());
  }

  /**
   * Translate this sprite to a new position (passed as a vector). Notifies obserrs with a TRANSLATE
   * event.
   *
   * @param v New position.
   */
  protected void setPosition(Vec2 v) {
    position = new SpriteCoordinates(v);

    notifyObservers(TRANSLATE);
  }

  /**
   * Direction that the Sprite is facing
   *
   * @return
   */
  public Vec2 getDirection() {
    return direction;
  }

  /**
   * Change the orientation of this Sprite. Notifies observers
   *
   * @param
   */
  protected void setDirection(Vec2 direction) {
    this.direction = direction;

    notifyObservers(ROTATE);
  }

  public boolean isVisible() {
    return true;
  }

  /**
   * Allows a Sprite to detect all objects that reside in the same tile as it does.  Each of these
   * other Sprites is given the opportunity to respond to coming into contact with this Sprite.
   *
   * @param state
   */
  public void handleCollisions(MutableGameState state) {
    List<Sprite> sprites = state.getCollidingWith(this);
    for (Sprite other : sprites) {
      this.uponHitBy(other, state);
      other.uponHitBy(this, state);
    }
  }

  /**
   * Sprites override this method to define game state changes or changes to the sprite upon coming
   * into contact with another Sprite.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   *              themselves from the game or adjust the score
   */
  public abstract void uponHitBy(Sprite other, MutableGameState state);

  /**
   * Adds an observer that will be notified whenever any of the subset of observedEvents occurs
   *
   * @param so             observer object to add
   * @param observedEvents events that this observer listens for
   */
  public void addObserver(SpriteObserver so, SpriteEvent.EventType... observedEvents) {
    SpriteEvent.EventType[] eventsToRegister =
        observedEvents.length == 0 ? SpriteEvent.EventType.values() : observedEvents;
    for (SpriteEvent.EventType observedEvent : eventsToRegister) {
      observers.get(observedEvent).add(so);
    }
  }

  /**
   * Removes an observer from listening to any event of this Sprite
   *
   * @param so observer object to remove
   */
  public void removeObserver(SpriteObserver so) {
    for (SpriteEvent.EventType observedEvent : observers.keySet()) {
      observers.get(observedEvent).remove(so);
    }
  }

  /**
   * Notify each observer that the following sprite events have occurred. If multiple events occur
   * simultaneously, an observer set to receive multiple types of events receives each separately.
   *
   * @param observedEvents collection of events to notify observers of.
   */
  protected void notifyObservers(SpriteEvent.EventType... observedEvents) {
    SpriteEvent.EventType[] eventsToRegister =
        observedEvents.length == 0 ? SpriteEvent.EventType.values() : observedEvents;
    for (SpriteEvent.EventType observedEvent : eventsToRegister) {
      for (SpriteObserver observer : observers.get(observedEvent)) {
        observer.onSpriteUpdate(new SpriteEvent(this, observedEvent));
      }
    }
  }

  // advance state by dt seconds
  public void step(double dt, MutableGameState pacmanGameState) {
    getCurrentAnimation().step(dt);
  }

  // TODO: Make not abstract to give a default
  public boolean mustBeConsumed(){
    return false;
  };

  public boolean isDeadlyToPacMan(){
    return false;
  };

  public boolean eatsGhosts(){
    return false;
  };

  public boolean isConsumable(){
    return true;
  };

  public boolean hasMultiplicativeScoring(){
    return false;
  };

  public abstract int getScore();

  @Override
  public final void respondToPowerEvent(PacmanPowerupEvent event){
    if (powerupOptions.containsKey(event)){
      powerupOptions.get(event).run();
    }
  }

  public SwapClass getSwapClass() {
    return swapClass;
  }

  public InputSource getDefaultInputSource() {
    return defaultInputSource;
  }

  public boolean needsSwap() {
    return inputSource.isActionPressed();
  }

  public InputSource getInputSource() {
    return null;
  }

  public void setInputSource(InputSource s) {
  }

  public String getInputString() {
    return inputString;
  }

  public void setInputString(String inputString) {
    this.inputString = inputString;
  }

  protected void setSwapClass(SwapClass swapClass) {
    this.swapClass = swapClass;
  }

  protected Map<PacmanPowerupEvent, Runnable> getPowerupOptions() {
    return powerupOptions;
  }

  protected void setPowerupOptions(
      Map<PacmanPowerupEvent, Runnable> powerupOptions) {
    this.powerupOptions = powerupOptions;
  }

  protected void setDefaultInputSource(InputSource defaultInputSource) {
    this.defaultInputSource = defaultInputSource;
  }
}
