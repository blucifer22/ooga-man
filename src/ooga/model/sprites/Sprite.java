package ooga.model.sprites;

import static ooga.model.api.SpriteEvent.EventType.ROTATE;
import static ooga.model.api.SpriteEvent.EventType.TRANSLATE;
import static ooga.model.api.SpriteEvent.EventType.TYPE_CHANGE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.api.GameEventObserver;
import ooga.model.api.InputSource;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteObserver;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.AnimationObserver;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * Sprites are things that exist on top of the grid, but are not pure UI elements such as score
 * labels.
 *
 * @author George Hong
 */
public abstract class Sprite implements ObservableSprite, GameEventObserver, AnimationObserver {

  private final SpriteAnimationFactory animationFactory;
  private final SpriteCoordinates initialPosition;
  private final Vec2 initialDirection;
  private final Map<GameEvent, Runnable> powerupOptions = new HashMap<>();
  private SwapClass swapClass;
  private InputSource inputSource;
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
   * @param spriteAnimationPrefix Prefix to use when constructing sprite-specific animations; e.g. "blinky"
   * @param startingAnimation Initial animation.
   * @param position Initial position.
   * @param direction Initial orientation vector.
   */
  protected Sprite(
      String spriteAnimationPrefix,
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

  /**
   * Construct a sprite from a SpriteDescription.
   * @param spriteAnimationPrefix Animation prefix to use when construcing sprite-specific animations.
   * @param startingAnimation Initial animation.
   * @param description Sprite description object.
   */
  protected Sprite(
      String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteDescription description) {
    this(spriteAnimationPrefix, startingAnimation, description.getCoordinates(), new Vec2(1, 0));
  }

  /**
   * Construct a sprite at the default starting location.
   * @param spriteAnimationPrefix Animation prefix to use.
   * @param startingAnimation Starting animation.
   */
  protected Sprite(
      String spriteAnimationPrefix, SpriteAnimationFactory.SpriteAnimationType startingAnimation) {
    this(spriteAnimationPrefix, startingAnimation, new SpriteCoordinates(), new Vec2(1, 0));
  }

  /**
   * Sprites can change properties based on the current round.
   *
   * @param roundNumber New round number.
   * @param state State to which this sprite belongs.
   */
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    // Does nothing (Overriden in specific child classes)
  }

  /**
   * This method resets the Sprite to its initial configuration at the start of a level. This method
   * can be called to reset to the start of a new level.
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
   *
   * @param state State from which to remove.
   */
  public void delete(MutableGameState state) {
    state.prepareRemove(this);
  }

  /**
   * Returns the type of this Sprite.
   *
   * <p>Cannot be overridden -- type changes must go through setCostumeIndex().
   *
   * @return Current costume type, as a string, like "pacman_halfopen".
   */
  public final String getCostume() {
    return currentAnimation.getCurrentCostume();
  }

  /**
   * Get the current animation of this sprite.
   * @return Current animation.
   */
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

  /**
   * Get the animation factorty associated with this sprite.
   * @return Animation factory.
   */
  protected SpriteAnimationFactory getAnimationFactory() {
    return animationFactory;
  }

  /**
   * Set the current animation type.
   * @param type Animation type enum.
   */
  protected void setCurrentAnimationType(SpriteAnimationFactory.SpriteAnimationType type) {
    if (type != currentAnimationType) {
      setCurrentAnimation(getAnimationFactory().createAnimation(type));
      currentAnimationType = type;
    }
  }

  /**
   * Called upon a costume change.
   * @param newCostume New costume name.
   */
  @Override
  public void onCostumeChange(String newCostume) {
    notifyObservers(TYPE_CHANGE);
  }

  // coordinates of the tile above which this sprite's center lies

  /**
   * Coordinates of this Sprite. Also provides the tile coordinates.
   *
   * @return Coordinates.
   */
  public SpriteCoordinates getCoordinates() {
    return position;
  }

  /**
   * Translate this sprite to a new set of coordinates.
   *
   * <p>Notifies observers.
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
   * @return Orientation.
   */
  public Vec2 getDirection() {
    return direction;
  }

  /**
   * Change the orientation of this Sprite. Notifies observers
   *
   * @param direction New direction.
   */
  protected void setDirection(Vec2 direction) {
    this.direction = direction;

    notifyObservers(ROTATE);
  }

  public boolean isVisible() {
    return true;
  }

  /**
   * Allows a Sprite to detect all objects that reside in the same tile as it does. Each of these
   * other Sprites is given the opportunity to respond to coming into contact with this Sprite.
   *
   * @param state Game state.
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
   *     themselves from the game or adjust the score
   */
  public abstract void uponHitBy(Sprite other, MutableGameState state);

  /**
   * Adds an observer that will be notified whenever any of the subset of observedEvents occurs
   *
   * @param so observer object to add
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

  /**
   * Advance this sprite's animation.
   * @param dt Time step.
   * @param pacmanGameState Game state.
   */
  public void step(double dt, MutableGameState pacmanGameState) {
    getCurrentAnimation().step(dt);
  }

  /**
   * Whether this sprite blocks level advancement.
   * @return Consumption required for level advancement if true.
   */
  public boolean mustBeConsumed() {
    return false;
  }

  /**
   * Query deadliness.
   * @return Deadliness.
   */
  public boolean isDeadlyToPacMan() {
    return false;
  }

  /**
   * Whether this sprite eats ghosts.
   * @return True if eats ghosts.
   */
  public boolean eatsGhosts() {
    return false;
  }

  /**
   * Whether this sprite is consumable.
   * @return True if consumable.
   */
  public boolean isConsumable() {
    return true;
  }

  /**
   * Whether this sprite should be scored multiplicatively.
   * @return True if it should be scored multiplicatively.
   */
  public boolean hasMultiplicativeScoring() {
    return false;
  }

  /**
   * Point value of this sprite.
   * @return Point value.
   */
  public int getScore() {
    return 0;
  }

  /**
   * Called on a game event.
   * @param event Event sent.
   */
  @Override
  public final void onGameEvent(GameEvent event) {
    if (powerupOptions.containsKey(event)) {
      powerupOptions.get(event).run();
    }
  }

  /**
   * Get swap class of this sprite.
   * @return Swap class.
   */
  public SwapClass getSwapClass() {
    return swapClass;
  }

  /**
   * Set swap class of this sprite.
   * @param swapClass Swap class.
   */
  protected void setSwapClass(SwapClass swapClass) {
    this.swapClass = swapClass;
  }

  /**
   * Get default input source of this sprite.
   * @return Default input source.
   */
  public InputSource getDefaultInputSource() {
    return defaultInputSource;
  }

  /**
   * Set default input source of this sprite.
   * @param defaultInputSource New default input source.
   */
  protected void setDefaultInputSource(InputSource defaultInputSource) {
    this.defaultInputSource = defaultInputSource;
  }

  /**
   * Whether this sprite is requesting an input swap.
   * @return True if swap requested.
   */
  public boolean needsSwap() {
    return inputSource.isActionPressed();
  }

  /**
   * Currently active input source of this sprite.
   * @return Input source.
   */
  public InputSource getInputSource() {
    return inputSource;
  }

  /**
   * Set the current input source.
   * @param s New input source.
   */
  public void setInputSource(InputSource s) {
    if (getDefaultInputSource() == null) {
      setDefaultInputSource(s);
    }
    inputSource = s;
  }

  /**
   * Get the string describing this sprite's input source.
   * @return Input source, as a string.
   */
  public String getInputString() {
    return inputString;
  }

  /**
   * Set this sprite's input source.
   * @param inputString Input string.
   */
  public void setInputString(String inputString) {
    this.inputString = inputString;
  }

  /**
   * Get the map of power-up event handlers.
   * @return Power-up event handlers.
   */
  protected Map<GameEvent, Runnable> getPowerupOptions() {
    return powerupOptions;
  }

  /**
   * Add power-up event handlers.
   * @param powerupOptions New handlers to add.
   */
  protected void addPowerUpOptions(Map<GameEvent, Runnable> powerupOptions) {
    this.powerupOptions.putAll(powerupOptions);
  }
}
