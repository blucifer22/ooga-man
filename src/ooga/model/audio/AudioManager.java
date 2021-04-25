package ooga.model.audio;

import ooga.model.GameEvent;
import ooga.model.api.AudioObserver;
import ooga.model.api.GameEventObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class AudioManager implements GameEventObserver {
  private Collection<AudioObserver> observers;
  private String currentAmbience = null;
  private String oldAmbience = null;
  private int frightenDepth = 0, eyesDepth = 0;

  private static final String NORMAL_AMBIENCE = "normal-loop";
  private static final String FRIGHT_AMBIENCE = "frightened-loop";

  public AudioManager() {
    observers = new ArrayList<>();
  }

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
   */
  public void playSound(String soundId) {
    forEachObserver(obs -> obs.onPlayOnce(soundId));
  }

  public void setAmbience(String soundId) {
    // make idempotent
    if(soundId.equals(currentAmbience))
      return;

    stopAmbience();

    currentAmbience = soundId;

    forEachObserver(obs -> obs.onPlayIndefinitely(currentAmbience));
  }

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

  public void pushNewAmbience(String newAmbience) {
    if(eyesDepth == 0)
      oldAmbience = currentAmbience;
    eyesDepth++;
    setAmbience(newAmbience);
  }

  public void popAmbience() {
    if(--eyesDepth == 0)
      setAmbience(oldAmbience);
  }

  public void reset() {
    frightenDepth = 0;
    eyesDepth = 0;
    setAmbience(NORMAL_AMBIENCE);
  }

  public void stopAmbience() {
    if(currentAmbience != null)
      forEachObserver(obs -> obs.onStop(currentAmbience));
  }

  @Override
  protected void finalize() throws Throwable {
    stopAmbience();
  }
}
