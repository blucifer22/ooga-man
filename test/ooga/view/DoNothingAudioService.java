package ooga.view;

import ooga.view.audio.AudioService;

public class DoNothingAudioService implements AudioService {

  @Override
  public void playOnce(String soundIdentifier) {

  }

  @Override
  public void playIndefinitely(String soundIdentifier) {

  }

  @Override
  public void pause(String soundIdentifier) {

  }

  @Override
  public void pauseAll() {

  }

  @Override
  public void stop(String soundIdentifier) {

  }

  @Override
  public void stopAll() {

  }
}
