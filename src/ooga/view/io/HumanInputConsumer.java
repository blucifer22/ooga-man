package ooga.view.io;

import javafx.scene.input.KeyCode;

/**
 * Interface for classes that consume input events from the frontend.
 *
 * <p>For example, when the user presses an arrow key, this interface's onKeyPress method gets
 * called with the appropriate JavaFX keycode.
 *
 * @author Franklin Wei
 * @author Marc Chmielewski
 */
public interface HumanInputConsumer {

  /**
   * This method handles the behavior associated with depressed keys and cues the calling class to
   * spin up an appropriate response.
   *
   * @param keyCode The KeyCode of the currently depressed key.
   */
  void onKeyPress(KeyCode keyCode);

  /**
   * This method handles the behavior associated with the release of a key
   *
   * @param keyCode The KeyCode of the key that is being released.
   */
  void onKeyRelease(KeyCode keyCode);
}
