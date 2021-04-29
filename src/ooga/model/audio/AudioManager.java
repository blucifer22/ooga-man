package ooga.model.audio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import ooga.model.GameEvent;
import ooga.model.api.AudioObserver;
import ooga.model.api.GameEventObserver;

/**
 * All backend audio playback goes through this class.
 *
 * This class manages both ambient and one-shot sounds.
 *
 * @author Franklin Wei
 */
public class AudioManager implements GameEventObserver {
  /**
   * Normal ambient noise.
   */
  public static final String NORMAL_AMBIENCE = "normal-loop";
  private static final String FRIGHT_AMBIENCE = "frightened-loop";
  private final Collection<AudioObserver> observers;
  private String currentAmbience = null;
  private String oldAmbience = null;
  private int frightenDepth = 0, eyesDepth = 0;

  /**
   * Construct an audio manager.
   */
  public AudioManager() {
    observers = new ArrayList<>();
  }

  /**
   * Add an audio observer.
   *
   * @param obs Observer.
   */
  public void addObserver(AudioObserver obs) {
    observers.add(obs);

    if(currentAmbience != null)
      obs.onPlayIndefinitely(currentAmbience);
  }

  private void forEachObserver(Consumer<AudioObserver> consumer) {
    observers.forEach(consumer);
  }

  /**
   * Play a sound once, starting immediately.
   *
   * @param soundId name of sound to play
   */
  public void playSound(String soundId) {
    forEachObserver(obs -> obs.onPlayOnce(soundId));
  }

  /**
   * Set the ambient sound.
   * @param soundId Ambient sound to loop.
   */
  public void setAmbience(String soundId) {
    // make idempotent
    if(soundId.equals(currentAmbience))
      return;

    stopAmbience();

    currentAmbience = soundId;

    forEachObserver(obs -> obs.onPlayIndefinitely(currentAmbience));
  }

  /**
   * Respond to a game event.
   * @param event Event.
   */
  @Override
  public void onGameEvent(GameEvent event) {
    switch(event) {
      case FRIGHTEN_ACTIVATED -> {
        frightenDepth++;
        setAmbience(FRIGHT_AMBIENCE);
      }
      case FRIGHTEN_DEACTIVATED -> {
        if(--frightenDepth <= 0)
          setAmbience(NORMAL_AMBIENCE);
      }
      case SPRITES_UNFROZEN -> {
        setAmbience(NORMAL_AMBIENCE);
      }
      case PACMAN_DEATH -> {
        stopAmbience();
      }
    }
  }

  /**
   * Add a new ambient sound to the stack.
   * @param newAmbience New ambient noise.
   */
  public void pushNewAmbience(String newAmbience) {
    if(eyesDepth == 0)
      oldAmbience = currentAmbience;
    eyesDepth++;
    setAmbience(newAmbience);
  }

  /**
   * Reduce ambient sound recursion.
   * NOTE: This does not actually implement a true stack at the moment,
   * nor does it need to -- we never go more than one deep.
   */
  public void popAmbience() {
    if(--eyesDepth == 0)
      setAmbience(oldAmbience);
  }

  /**
   * Reset audio recursion depth.
   */
  public void reset() {
    frightenDepth = 0;
    eyesDepth = 0;
  }

  /**
   * Stop ambient sounds.
   */
  public void stopAmbience() {
    if(currentAmbience != null)
      forEachObserver(obs -> obs.onStop(currentAmbience));
    currentAmbience = null;
  }
}
