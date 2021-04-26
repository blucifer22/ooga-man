package ooga.view.views.components.audioplayer;

import ooga.model.api.AudioObserver;
import ooga.view.audio.AudioService;

public class ViewBoundAudioPlayer implements AudioObserver {

  private final AudioService audioService;

  public ViewBoundAudioPlayer(AudioService audioService) {
    this.audioService = audioService;
  }

  @Override
  public void onPlayOnce(String soundIdentifier) {
    this.audioService.playOnce(soundIdentifier);
  }

  @Override
  public void onPlayIndefinitely(String soundIdentifier) {
    this.audioService.playIndefinitely(soundIdentifier);
  }

  @Override
  public void onPause(String soundIdentifier) {
    this.audioService.pause(soundIdentifier);
  }

  @Override
  public void onPauseAll() {
    this.audioService.pauseAll();
  }

  @Override
  public void onStop(String soundIdentifier) {
    this.audioService.stop(soundIdentifier);
  }

  @Override
  public void onStopAll() {
    this.audioService.stopAll();
  }
}
