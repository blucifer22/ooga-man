package ooga.model.sprites.animation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class defines the abstract notion of a sprite "animation."
 *
 * <p>Animations describe how sprites change appearance over time -- Pac-Man, for instance, is
 * always performing the "chomp-chomp" animation (except after being eaten by a ghost), while ghosts
 * can switch between different animations depending on their direction of travel, and "frightened"
 * status.
 *
 * <p>Generic sprite animations need not be periodic, but the only current implementation of
 * SpriteAnimation is PeriodicAnimation, which is periodic.
 *
 * @author Franklin Wei
 */
public abstract class SpriteAnimation implements ObservableAnimation {
  private final Set<AnimationObserver> observers;

  private String costume;

  private boolean paused;

  private double relativeSpeed;

  /**
   * Initialize an animation to an initial costume.
   * @param initialCostume Initial costume.
   */
  protected SpriteAnimation(String initialCostume) {
    observers = new HashSet<>();
    costume = initialCostume;
    paused = false;

    setRelativeSpeed(1);
  }

  /**
   * Retrieve current costume.
   * @return Current costume.
   */
  public final String getCurrentCostume() {
    return costume;
  }

  /**
   * Set costume.
   * @param c New costume.
   */
  protected void setCostume(String c) {
    costume = c;

    notifyObservers(ao -> ao.onCostumeChange(c));
  }

  /**
   * Advance animation through time.
   * @param dt Time step.
   */
  public abstract void step(double dt);

  /**
   * Query paused state.
   * @return True if paused.
   */
  public final boolean isPaused() {
    return paused;
  }

  /**
   * (Un) pause this animation.
   * @param paused whether to pause
   */
  public final void setPaused(boolean paused) {
    this.paused = paused;
  }

  /**
   * Add an observer.
   * @param ao observer.
   */
  public void addObserver(AnimationObserver ao) {
    observers.add(ao);
  }

  /**
   * Remove observer.
   * @param ao Observer to remove.
   */
  public void removeObserver(AnimationObserver ao) {
    observers.remove(ao);
  }

  /**
   * Notify observers of events.
   * @param consumer Lambda to invoke.
   */
  protected void notifyObservers(Consumer<AnimationObserver> consumer) {
    for (AnimationObserver ao : observers) consumer.accept(ao);
  }

  /**
   * Get relative speed of this animation.
   * @return Relative speed.
   */
  @Override
  public double getRelativeSpeed() {
    return relativeSpeed;
  }

  /**
   * SEt relative speed of this animation.
   * @param relativeSpeed factor by which to speed up.
   */
  @Override
  public void setRelativeSpeed(double relativeSpeed) {
    this.relativeSpeed = relativeSpeed;
  }
}
