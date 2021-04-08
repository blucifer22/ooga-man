package ooga.view.theme.serialized;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.leveldescription.JSONDescription;
import ooga.view.theme.api.Costume;

public class CostumeDescription extends JSONDescription {
  private String fill;
  private boolean fillIsImage;
  private double scale;
  private boolean bottomHeavy;

  public CostumeDescription(CostumeDescription description) {
    this.fill = description.fill;
    this.fillIsImage = description.fillIsImage;
    this.scale = description.scale;
    this.bottomHeavy = description.bottomHeavy;
  }

  public CostumeDescription(
      @JsonProperty("fill") String fill,
      @JsonProperty("imageFill") boolean fillIsImage,
      @JsonProperty("scale") double scale,
      @JsonProperty("bottomHeavy") boolean bottomHeavy
  ) {
    this.scale = scale;
    this.bottomHeavy = bottomHeavy;
    this.fillIsImage = fillIsImage;
    this.fill = fill;
  }

  @JsonGetter("fill")
  public String getFill() {
    return this.fill;
  }

  @JsonGetter("imageFill")
  public boolean isImage() {
    return this.fillIsImage;
  }

  @JsonGetter("scale")
  public double getScale() {
    return this.scale;
  }

  @JsonGetter("bottomHeavy")
  public boolean isBottomHeavy() {
    return this.bottomHeavy;
  }

  public void setFill(String fill, boolean fillIsImage) {
    this.fill = fill;
    this.fillIsImage = fillIsImage;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public void setBottomHeavy(boolean bottomHeavy) {
    this.bottomHeavy = bottomHeavy;
  }

  public Costume toCostume() {
    return new DeserializedCostume(this);
  }
}
