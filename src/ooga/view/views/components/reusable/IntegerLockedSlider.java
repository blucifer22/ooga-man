package ooga.view.views.components.reusable;

import java.util.HashSet;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Slider;

/**
 * A small extension to the original JavaFX slider class that locks slider values to integers and
 * allows for {@link IntegerValueObserver}s to observe the <em>integer</em> values of the slider.d
 *
 * @author David Coffman
 */
public class IntegerLockedSlider extends Slider {

  private final HashSet<IntegerValueObserver> changeListeners;

  /**
   * Sole {@link IntegerLockedSlider} constructor. Constructs a slider identical to that
   * constructed by the {@link Slider} superclass, except that it automatically locks double
   * values to the nearet integer value.
   *
   * @param min the minimum bound for the slider range
   * @param max the maximum bound for the slider range
   * @param defaultVal the default value of the slider
   */
  public IntegerLockedSlider(int min, int max, int defaultVal) {
    super(min, max, defaultVal);
    this.changeListeners = new HashSet<>();
    this.valueProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              int roundedValue = (int) Math.round(newVal.doubleValue());
              if (roundedValue != newVal.doubleValue()) {
                ((DoubleProperty) obs).setValue(roundedValue);
                for (IntegerValueObserver observer : changeListeners) {
                  observer.consume(roundedValue);
                }
              }
            });
  }

  /**
   * Adds an {@link IntegerValueObserver} as an observer.
   *
   * @param valueObserver the {@link IntegerValueObserver} to add as an observer.
   */
  public void addListener(IntegerValueObserver valueObserver) {
    changeListeners.add(valueObserver);
  }

  @FunctionalInterface
  public interface IntegerValueObserver {

    /**
     * Called when the {@link IntegerLockedSlider} changes value.
     *
     * @param newVal the new value
     */
    void consume(int newVal);
  }
}
