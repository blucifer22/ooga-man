package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.theme.api.Costume;

/**
 * Serialization description implementation for a {@link Costume}, as part of the
 * serialization/deserialization scheme in {@link ooga.view.theme.serialized}. Equates all
 * {@link Costume} parameters to primitives.
 *
 * @author David Coffman
 */
public class CostumeDescription extends JSONDescription {

  private final String fill;
  private final boolean fillIsImage;
  private final double scale;
  private final boolean bottomHeavy;
  private final boolean rotatable;

  /**
   * Sole constructor. Called only during deserialization.
   *
   * @param fill a {@link String} representing the fill
   * @param fillIsImage <code>true</code> if the fill is an image, <code>false</code> if a color
   * @param scale the object scale
   * @param bottomHeavy see {@link Costume}
   * @param rotatable see {@link Costume}
   */
  public CostumeDescription(
      @JsonProperty("fill") String fill,
      @JsonProperty("imageFill") boolean fillIsImage,
      @JsonProperty("scale") double scale,
      @JsonProperty("bottomHeavy") boolean bottomHeavy,
      @JsonProperty("rotates") boolean rotatable) {
    this.scale = scale;
    this.bottomHeavy = bottomHeavy;
    this.fillIsImage = fillIsImage;
    this.fill = fill;
    this.rotatable = rotatable;
  }

  /**
   * Shallow copy constructor.
   *
   * @param description the {@link CostumeDescription} to copy
   */
  public CostumeDescription(CostumeDescription description) {
    this.fill = description.fill;
    this.fillIsImage = description.fillIsImage;
    this.scale = description.scale;
    this.bottomHeavy = description.bottomHeavy;
    this.rotatable = description.rotatable;
  }

  /**
   * Returns the fill for this {@link CostumeDescription}'s {@link Costume}.
   *
   * @return the fill for this {@link CostumeDescription}'s {@link Costume}.
   */
  @JsonGetter("fill")
  public String getFill() {
    return this.fill;
  }

  /**
   * Returns whether the fill for this {@link CostumeDescription}'s {@link Costume} is an image.
   *
   * @return whether the fill for this {@link CostumeDescription}'s {@link Costume} is an image.
   */
  @JsonGetter("imageFill")
  public boolean isImage() {
    return this.fillIsImage;
  }

  /**
   * Returns the scale for this {@link CostumeDescription}'s {@link Costume}.
   *
   * @return the scale for this {@link CostumeDescription}'s {@link Costume}.
   */
  @JsonGetter("scale")
  public double getScale() {
    return this.scale;
  }

  /**
   * Returns whether the fill for this {@link CostumeDescription}'s {@link Costume} is bottom
   * -heavy (see{@link Costume}).
   *
   * @return whether the fill for this {@link CostumeDescription}'s {@link Costume} is bottom-heavy
   */
  @JsonGetter("bottomHeavy")
  public boolean isBottomHeavy() {
    return this.bottomHeavy;
  }

  /**
   * Returns whether this {@link CostumeDescription}'s {@link Costume} rotates.
   *
   * @return whether this {@link CostumeDescription}'s {@link Costume} rotates.
   */
  @JsonGetter("rotates")
  public boolean isRotatable() {
    return this.rotatable;
  }

  /**
   * Returns this object in {@link Costume} form, for use by views.
   *
   * @return this object in {@link Costume} form, for use by views.
   */
  public Costume toCostume() {
    return new SerializedCostume(this);
  }
}
