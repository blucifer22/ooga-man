package ooga.model.sprites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.InputSource;
import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

public abstract class MoveableSprite extends Sprite {

  private InputSource inputSource;
  private double currentSpeed;
  private double movementSpeed;
  private Vec2 queuedDirection;

  @JsonCreator
  public MoveableSprite(
      @JsonProperty("position") SpriteCoordinates position,
      @JsonProperty("direction") Vec2 direction,
      @JsonProperty("speed") double speed) {
    super(position, direction);
    this.currentSpeed = 0;
    this.movementSpeed = speed;
    queuedDirection = null;
  }

  public MoveableSprite(SpriteDescription description) {
    super(description);
  }

  public double getMovementSpeed() {
    return movementSpeed;
  }

  public double getCurrentSpeed() {
    return currentSpeed;
  }

  public void setMovementSpeed(double speed) {
    this.movementSpeed = speed;
  }

  protected InputSource getInputSource() {
    return inputSource;
  }

  public void setInputSource(InputSource s) {
    inputSource = s;
  }

  protected abstract boolean canMoveTo(Tile tile);

  public void move(double dt, PacmanGrid grid){
    Vec2 userDirection = getInputSource().getRequestedDirection();

    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
      currentSpeed = movementSpeed;
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }

    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getPosition();
    Vec2 nextPosition = currentPosition.add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));

    // Grid-snapping
    if (centerCoordinates.isBetween(currentPosition, nextPosition)) {
      setPosition(centerCoordinates);
      TileCoordinates currentTile = getCoordinates().getTileCoordinates();
      // Tile target assuming use of queued direction
      TileCoordinates newTargetTile =
          queuedDirection == null
              ? new TileCoordinates(0, 0)
              : new TileCoordinates(
                  currentTile.getX() + (int) queuedDirection.getX(),
                  currentTile.getY() + (int) queuedDirection.getY());
      // Tile target assuming continued use of current direction
      TileCoordinates currentTargetTile =
          new TileCoordinates(
              currentTile.getX() + (int) getDirection().getX(),
              currentTile.getY() + (int) getDirection().getY());

      boolean isTargetOpen = canMoveTo(grid.getTile(newTargetTile));
      boolean isCurrentOpen = canMoveTo(grid.getTile(currentTargetTile));
      if (queuedDirection != null && isTargetOpen) {
        setDirection(queuedDirection);
        queuedDirection = null;
        currentSpeed = movementSpeed;
      } else if (!isCurrentOpen) {
        currentSpeed = 0;
      }
    }

    nextPosition =
        getCoordinates().getPosition().add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));

    setPosition(nextPosition);
  }
}
