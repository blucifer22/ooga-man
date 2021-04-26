package ooga.model.leveldescription;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ooga.model.api.AudioObserver;
import ooga.model.audio.AudioManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AudioManagerTest implements AudioObserver {
  AudioManager manager;
  String play, loop, stop;

  @BeforeEach
  void init() {
    manager = new AudioManager();
    play = null;
    manager.addObserver(this);
  }

  @Test
  void testPlay() {
    manager.playSound("test");
    assertEquals("test", play);
  }

  @Test
  void testAmbience() {
    manager.setAmbience("normal");
    assertEquals("normal", loop);

    manager.pushNewAmbience("frighten");
    assertEquals("frighten", loop);

    manager.popAmbience();

    assertEquals("normal", loop);

    manager.stopAmbience();
    assertEquals("normal", stop);
  }

  @Override
  public void onPlayOnce(String soundIdentifier) {
    play = soundIdentifier;
  }

  @Override
  public void onPlayIndefinitely(String soundIdentifier) {
    loop = soundIdentifier;
  }

  @Override
  public void onPause(String soundIdentifier) {}

  @Override
  public void onPauseAll() {}

  @Override
  public void onStop(String soundIdentifier) {
    stop = soundIdentifier;
  }

  @Override
  public void onStopAll() {}
}
