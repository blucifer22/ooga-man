package ooga.view.views.components.audioplayer;

import ooga.model.api.AudioObserver;
import ooga.view.audio.AudioService;

/**
 * Middleware class that triggers {@link AudioService} methods in response to
 * {@link AudioObserver} callbacks. Essentially responsible for linking the frontend and
 * backend's audio interfaces.
 *
 * @author David Coffman
 */
public class ViewBoundAudioPlayer implements AudioObserver {

  private final AudioService audioService;

  /**
   * Sole constructor for {@link ViewBoundAudioPlayer}. Takes an {@link AudioService}, to which
   * the player's audio playing requests are directed.
   *
   * @param audioService {@link AudioService} to which the player's audio playing requests should
   *                                        be directed.
   */
  public ViewBoundAudioPlayer(AudioService audioService) {
    this.audioService = audioService;
  }

  /**
   * Plays a sound with the parameter identifier exactly one time.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  @Override
  public void onPlayOnce(String soundIdentifier) {
    this.audioService.playOnce(soundIdentifier);
  }

  /**
   * Plays a sound with the parameter identifier until stopped. The sound repeats automatically
   * when finished.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  @Override
  public void onPlayIndefinitely(String soundIdentifier) {
    this.audioService.playIndefinitely(soundIdentifier);
  }

  /**
   * Pauses all running sounds with the parameter identifier. Sounds playing indefinitely will
   * pause, but will resume playing indefinitely once resumed.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  @Override
  public void onPause(String soundIdentifier) {
    this.audioService.pause(soundIdentifier);
  }

  /**
   * Pauses all running sounds. Sounds playing indefinitely will pause, but will resume playing
   * indefinitely once resumed.
   */
  @Override
  public void onPauseAll() {
    this.audioService.pauseAll();
  }

  /**
   * Stops all running sounds with the parameter identifier. Sounds playing indefinitely will
   * stop.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  @Override
  public void onStop(String soundIdentifier) {
    this.audioService.stop(soundIdentifier);
  }

  /**
   * Stops all running sounds. Sounds playing indefinitely will stop.
   */
  @Override
  public void onStopAll() {
    this.audioService.stopAll();
  }
}
