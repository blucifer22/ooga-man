package ooga.model.sprites.animation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class defines the abstract notion of a sprite "animation."
 *
 * Animations describe how sprites change appearance over time --
 * Pac-Man, for instance, is always performing the "chomp-chomp"
 * animation (except after being eaten by a ghost), while ghosts can
 * switch between different animations depending on their direction of
 * travel, and "frightened" status.
 *
 * Generic sprite animations need not be periodic, but the only
 * current implementation of SpriteAnimation is PeriodicAnimation,
 * which is periodic.
 *
 * @author Franklin Wei
 */
public abstract class SpriteAnimation implements ObservableAnimation {
  private Set<AnimationObserver> observers;

  private String costume;

  private boolean paused;

  protected SpriteAnimation(String initialCostume) {
    observers = new HashSet<>();
    costume = initialCostume;
    paused = false;
  }

  public final String getCurrentCostume() {
    return costume;
  }

  protected void setCostume(String c) {
    costume = c;

    notifyObservers(ao -> ao.onCostumeChange(c));
  }

  public abstract void step(double dt);

  public final void setPaused(boolean paused) {
    this.paused = paused;
  }

  public final boolean isPaused() {
    return paused;
  }

  public void addObserver(AnimationObserver ao) {
    observers.add(ao);
  }

  public void removeObserver(AnimationObserver ao) {
    observers.remove(ao);
  }

  protected void notifyObservers(Consumer<AnimationObserver> consumer) {
    for(AnimationObserver ao : observers)
      consumer.accept(ao);
  }
}
