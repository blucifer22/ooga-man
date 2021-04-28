package ooga.view.audio;

/**
 * View-facing interface for a class providing audio services to referencing objects.
 *
 * @author David Coffman
 */
public interface AudioService {

  /**
   * Plays a sound with the parameter identifier exactly one time.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  void playOnce(String soundIdentifier);

  /**
   * Plays a sound with the parameter identifier until stopped. The sound repeats automatically when
   * finished.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  void playIndefinitely(String soundIdentifier);

  /**
   * Pauses all running sounds with the parameter identifier. Sounds playing indefinitely will
   * pause, but will resume playing indefinitely once resumed.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  void pause(String soundIdentifier);

  /**
   * Pauses all running sounds. Sounds playing indefinitely will pause, but will resume playing
   * indefinitely once resumed.
   */
  void pauseAll();

  /**
   * Stops all running sounds with the parameter identifier. Sounds playing indefinitely will stop.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  void stop(String soundIdentifier);

  /**
   * Stops all running sounds. Sounds playing indefinitely will stop.
   */
  void stopAll();
}
