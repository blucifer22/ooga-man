package ooga.controller;

import java.util.Arrays;
import java.util.HashSet;
import javafx.scene.input.KeyCode;
import ooga.view.io.HumanInputConsumer;

/**
 * A composite pattern implementation for handling multiple {@link HumanInputConsumer}s. Used for
 * game modes where multiple human input consumers need to be supplied to the
 * {@link ooga.view.UIController}.
 *
 * @author David Coffman
 */
public class HumanInputConsumerComposite implements HumanInputConsumer {

  private final HashSet<HumanInputConsumer> components = new HashSet<>();

  /**
   * Default varargs constructor. Supplied with an initial varargs list of
   * {@link HumanInputConsumer}s, which become the composite's initial consumers.
   *
   * @param consumers the composite's initial consumers
   */
  public HumanInputConsumerComposite(HumanInputConsumer... consumers) {
    components.addAll(Arrays.asList(consumers));
  }

  /**
   * Adds one or more single {@link HumanInputConsumer}s to the composite.
   *
   * @param consumers the consumers to add to the composite
   */
  public void addConsumers(HumanInputConsumer... consumers) {
    components.addAll(Arrays.asList(consumers));
  }

  /**
   * Removes one or more single {@link HumanInputConsumer}s to the composite. Removes only the
   * {@link HumanInputConsumer}s actually managed by this object. Does not fail if supplied with
   * consumers that are not managed by this object, however.
   *
   * @param consumers the consumers to remove from the composite
   */
  public void removeConsumers(HumanInputConsumer... consumers) {
    Arrays.asList(consumers).forEach(components::remove);
  }

  public void clearConsumers() {
    components.clear();
  }

  /**
   * This method handles the behavior associated with depressed keys and cues the calling class to
   * spin up an appropriate response.
   *
   * @param keyCode The KeyCode of the currently depressed key.
   */
  @Override
  public void onKeyPress(KeyCode keyCode) {
    for (HumanInputConsumer consumer : components) {
      consumer.onKeyPress(keyCode);
    }
  }

  /**
   * This method handles the behavior associated with the release of a key
   *
   * @param keyCode The KeyCode of the key that is being released.
   */
  @Override
  public void onKeyRelease(KeyCode keyCode) {
    for (HumanInputConsumer consumer : components) {
      consumer.onKeyRelease(keyCode);
    }
  }
}
