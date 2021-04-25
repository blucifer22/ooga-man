package ooga.view.views.components;

import java.util.HashSet;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Slider;

public class IntegerLockedSlider extends Slider {
  private final HashSet<IntegerValueObserver> changeListeners;

  public IntegerLockedSlider(int min, int max, int defaultVal) {
    super(min, max, defaultVal);
    this.changeListeners = new HashSet<>();
    this.valueProperty().addListener((obs, oldVal, newVal) -> {
      int roundedValue = (int) Math.round(newVal.doubleValue());
      if (roundedValue != newVal.doubleValue()) {
        ((DoubleProperty) obs).setValue(roundedValue);
        for (IntegerValueObserver observer: changeListeners) {
          observer.consume(roundedValue);
        }
      }
    });
  }

  public void addListener(IntegerValueObserver valueObserver) {
    changeListeners.add(valueObserver);
  }

  @FunctionalInterface
  public interface IntegerValueObserver {
    void consume(int newVal);
  }
}
