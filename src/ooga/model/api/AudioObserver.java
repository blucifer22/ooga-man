package ooga.model.api;

/**
 * Interface for receiving audio events from the backend.
 */
public interface AudioObserver {
  /**
   * Called upon a one-shot sound.
   * @param soundIdentifier Sound name.
   */
  void onPlayOnce(String soundIdentifier);

  /**
   * Called upon starting a looping sound.
   * @param soundIdentifier Sound name.
   */
  void onPlayIndefinitely(String soundIdentifier);

  /**
   * Called on pausing a sound.
   * @param soundIdentifier Sound to pause.
   */
  void onPause(String soundIdentifier);

  /**
   * Called on pausing all sounds.
   */
  void onPauseAll();

  /**
   * Called upon stopping a sound.
   * @param soundIdentifier Sound name.
   */
  void onStop(String soundIdentifier);

  /**
   * Called upon stopping all sounds.
   */
  void onStopAll();
}
