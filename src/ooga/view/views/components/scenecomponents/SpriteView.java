package ooga.view.views.components.scenecomponents;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent;
import ooga.model.api.SpriteObserver;
import ooga.model.grid.SpriteCoordinates;
import ooga.util.Vec2;
import ooga.view.internal_api.Renderable;
import ooga.view.theme.api.Costume;
import ooga.view.theme.api.Theme;
import ooga.view.theme.api.ThemeService;
import ooga.view.theme.api.ThemedObject;

/**
 * Handles the rendering of a single {@link ObservableSprite}, by responding (through
 * observation) to changes in its rotation, visibility, and type. Also responds to theme changes
 * and updates the view's {@link Costume} accordingly.
 *
 * @author David Coffman
 */
public class SpriteView implements SpriteObserver, ThemedObject, Renderable {

  private final Rectangle viewGraphic;
  private final ObservableSprite dataSource;
  private final DoubleProperty size;
  private final ThemeService themeService;
  private Costume costume;

  /**
   * Sole {@link SpriteView} constructor.
   *
   * @param so the {@link ObservableSprite} to act as this view's data source
   * @param themeService the {@link ThemeService} to query for a {@link Costume}
   * @param size the bound size of this view
   */
  public SpriteView(ObservableSprite so, ThemeService themeService, DoubleProperty size) {
    // configure data sourcing
    so.addObserver(this);
    this.dataSource = so;
    this.size = size;

    // render
    this.viewGraphic = new Rectangle();
    this.viewGraphic.setFill(Color.BLUE);
    this.viewGraphic.widthProperty().bind(size);
    this.viewGraphic.heightProperty().bind(size);

    // theme service
    this.themeService = themeService;
    this.themeService.addThemedObject(this);

    // initial positioning
    updateType();
    updatePosition();
    updateOrientation();
    updateVisibility();
  }

  /**
   * Called when a sprite event occurs. Updates the {@link SpriteView}'s state to match that of
   * the observed {@link ObservableSprite}.
   *
   * @param e Event that occurred.
   */
  @Override
  public void onSpriteUpdate(SpriteEvent e) {
    switch (e.getEventType()) {
      case TYPE_CHANGE -> updateType();
      case TRANSLATE -> updatePosition();
      case ROTATE -> updateOrientation();
      case VISIBILITY -> updateVisibility();
    }
  }

  /**
   * Observer callback for {@link ThemedObject}s. Called when the theme changes. Re-queries the
   * {@link ThemeService} for a new {@link Theme} when this method is called.
   */
  @Override
  public void onThemeChange() {
    updateType();
  }

  // Changes the SpriteView's costume in response to a type change event.
  private void updateType() {
    this.costume = themeService.getTheme().getCostumeForObjectOfType(dataSource.getCostume());

    this.viewGraphic.setFill(this.costume.getFill());
    this.viewGraphic.widthProperty().bind(size.multiply(this.costume.getScale()));
    this.viewGraphic.heightProperty().bind(size.multiply(this.costume.getScale()));

    this.updateOrientation();
    this.updatePosition();
  }

  // Updates the SpriteView's position in response to a position change event.
  private void updatePosition() {
    SpriteCoordinates coordinates = dataSource.getCoordinates();
    this.viewGraphic.translateXProperty()
        .bind(size.multiply(coordinates.getPosition().getX() - 0.5 * this.costume.getScale()));
    this.viewGraphic.translateYProperty()
        .bind(size.multiply(coordinates.getPosition().getY() - 0.5 * this.costume.getScale()));
  }

  // Updates the SpriteView's orientation in response to a rotation event.
  private void updateOrientation() {
    double rotation = 0;
    double scaleX = 1;

    if (this.costume.isRotatable()) {
      Vec2 direction = dataSource.getDirection();
      rotation = Math.atan2(direction.getY(), direction.getX()) * 180.0 / Math.PI;
      if (rotation > 90 && rotation <= 270) {
        scaleX = -1;
        rotation = ((rotation - 180) + 180) % 180;
      }
    }

    this.viewGraphic.setScaleX(scaleX);
    this.viewGraphic.setRotate(rotation);
  }

  // Updates the SpriteView's visibility in response to a visibility change event.
  private void updateVisibility() {
    viewGraphic.setVisible(dataSource.isVisible());
  }

  /**
   * Returns the {@link SpriteView}'s managed {@link Node}.
   *
   * @return the {@link SpriteView}'s managed {@link Node}.
   */
  @Override
  public Node getRenderingNode() {
    return this.viewGraphic;
  }
}