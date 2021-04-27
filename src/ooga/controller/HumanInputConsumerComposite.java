package ooga.controller;

import java.util.Arrays;
import java.util.HashSet;
import javafx.scene.input.KeyCode;
import ooga.view.io.HumanInputConsumer;

/**
 * A composite human input consumer that feeds into several input consumers.
 *
 * @author David Coffman
 */
public class HumanInputConsumerComposite implements HumanInputConsumer {

  private final HashSet<HumanInputConsumer> components = new HashSet<>();

  /**
   * Construct a input consumer composite.
   * @param consumers Consumers in this composite.
   */
  public HumanInputConsumerComposite(HumanInputConsumer... consumers) {
    components.addAll(Arrays.asList(consumers));
  }

  /**
   * Add consumers.
   * @param consumers More consumers to add.
   */
  public void addConsumers(HumanInputConsumer... consumers) {
    components.addAll(Arrays.asList(consumers));
  }

  /**
   * Remove consumers.
   * @param consumers Consumers to remove.
   */
  public void removeConsumers(HumanInputConsumer... consumers) {
    Arrays.asList(consumers).forEach(components::remove);
  }

  /**
   * Remove all consumers.
   */
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
