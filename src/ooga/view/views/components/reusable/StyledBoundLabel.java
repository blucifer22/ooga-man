package ooga.view.views.components.reusable;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.Label;

/**
 * A label with built-in style classes and an {@link javafx.beans.Observable}-bound label text.
 *
 * @author David Coffman
 */
public class StyledBoundLabel extends Label {

  /**
   * Constructs a {@link StyledBoundLabel} using a label binding and label style class.
   *
   * @param labelBinding the {@link StringBinding} to bind the label to
   * @param labelClass   the style class for the label
   */
  public StyledBoundLabel(StringBinding labelBinding, String labelClass) {
    this.textProperty().bind(labelBinding);
    style(labelClass);
  }

  /**
   * Constructs a {@link StyledBoundLabel} using a label binding and label style class.
   *
   * @param labelBinding the {@link StringBinding} to bind the label to
   * @param labelClass   the style class for the label
   * @param id           the style ID for the label
   */
  public StyledBoundLabel(StringBinding labelBinding, String labelClass, String id) {
    this(labelBinding, labelClass);
    this.setId("label-" + id);
  }

  /**
   * Constructs a {@link StyledBoundLabel} using a label property and label style class.
   *
   * @param labelProperty the {@link ReadOnlyStringProperty} to bind the label to
   * @param labelClass    the style class for the label
   */
  public StyledBoundLabel(ReadOnlyStringProperty labelProperty, String labelClass) {
    this.textProperty().bind(labelProperty);
    style(labelClass);
  }

  /**
   * Constructs a {@link StyledBoundLabel} using a label property and label style class.
   *
   * @param labelProperty the {@link ReadOnlyStringProperty} to bind the label to
   * @param labelClass    the style class for the label
   * @param id            the style ID for the label
   */
  public StyledBoundLabel(ReadOnlyStringProperty labelProperty, String labelClass, String id) {
    this(labelProperty, labelClass);
    this.setId("label-" + id);
  }

  private void style(String labelClass) {
    this.getStyleClass().addAll("styled-label", "styled-label-" + labelClass);
  }

  /**
   * Builder pattern implementation of text wrapping.
   *
   * @param wrap whether to wrap text at EOL
   * @return <code>this</code>
   */
  public StyledBoundLabel wrap(boolean wrap) {
    this.setWrapText(wrap);
    return this;
  }
}
