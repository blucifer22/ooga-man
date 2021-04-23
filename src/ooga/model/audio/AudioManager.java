package ooga.model.audio;

import ooga.model.PacmanPowerupEvent;
import ooga.model.api.AudioObserver;
import ooga.model.api.PowerupEventObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class AudioManager implements PowerupEventObserver {
  private Collection<AudioObserver> observers;
  private String currentAmbience = null;
  private int frightenDepth;

  private static final String NORMAL_AMBIENCE = "normal-loop";
  private static final String FRIGHT_AMBIENCE = "frightened-loop";

  public AudioManager() {
    observers = new ArrayList<>();
    reset();
  }

  public void addObserver(AudioObserver obs) {
    observers.add(obs);
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
    if(currentAmbience != null)
      forEachObserver(obs -> obs.onStop(currentAmbience));

    // make idempotent
    if(soundId.equals(currentAmbience))
      return;

    currentAmbience = soundId;

    forEachObserver(obs -> obs.onPlayIndefinitely(currentAmbience));
  }

  @Override
  public void respondToPowerEvent(PacmanPowerupEvent event) {
    switch(event) {
      case FRIGHTEN_ACTIVATED -> {
        frightenDepth++;
        setAmbience(FRIGHT_AMBIENCE);
      }
      case FRIGHTEN_DEACTIVATED -> {
        if(--frightenDepth <= 0)
          setAmbience(NORMAL_AMBIENCE);
      }
    }
  }

  public void reset() {
    frightenDepth = 0;
    setAmbience(NORMAL_AMBIENCE);
  }
}
