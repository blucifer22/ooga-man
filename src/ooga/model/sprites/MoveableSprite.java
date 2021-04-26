package ooga.model.sprites;

import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.*;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;
import java.util.Map;

/**
 * @author George Hong
 */
public abstract class MoveableSprite extends Sprite {

  public static final int UNIVERSAL_MAX_MOVEMENT_SPEED = 6;
  private double currentSpeed;
  private double movementSpeed;
  private Vec2 queuedDirection;
  private boolean frozen;

  private double initialMovementSpeed;

  protected MoveableSprite(String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteCoordinates position,
      Vec2 direction,
      double speed) {
    super(spriteAnimationPrefix, startingAnimation, position, direction);
    this.currentSpeed = 0;
    this.movementSpeed = speed;
    queuedDirection = null;
    frozen = true;
    addPowerUpOptions(Map.of(GameEvent.SPRITES_UNFROZEN, this::unfreeze));
    initialMovementSpeed = speed;
  }

  protected MoveableSprite(String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteDescription description) {
    super(spriteAnimationPrefix, startingAnimation, description);
  }

  @Override
  public void reset() {
    super.reset();
    movementSpeed = initialMovementSpeed;
  }

  public double getMovementSpeed() {
    return movementSpeed;
  }

  public void setMovementSpeed(double speed) {
    this.movementSpeed = speed;
  }

  public double getCurrentSpeed() {
    return currentSpeed;
  }

  public void setCurrentSpeed(double speed) {
    this.currentSpeed = speed;
  }

  /**
   * Moveable Sprites can update their speed with every new round to increase the difficulty of the
   * Pac-Man game.  Additionally, a maximum movement speed is present to cap Sprites from moving too
   * quickly.
   *
   * @param roundNumber current round of Pac-Man.
   * @param state
   */
  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    movementSpeed = Math
        .min(initialMovementSpeed + 0.5 * roundNumber, UNIVERSAL_MAX_MOVEMENT_SPEED);
    frozen = true;
  }

  protected abstract boolean canMoveTo(Tile tile);

  public void unfreeze() {
    frozen = false;
  }

  public void freeze() {
    frozen = true;
  }

  public void move(double dt, PacmanGrid grid) {
    if (frozen) {
      return;
    }
    Vec2 userDirection = getInputSource().getRequestedDirection(dt);
    userDirection = userDirection.getMagnitude() == 1 ? userDirection : Vec2.ZERO;
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
      currentSpeed = movementSpeed;
      //System.out.println(movementSpeed);
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }
    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getPosition();
    Vec2 nextPosition = currentPosition
        .add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));
    // Grid-snapping
    checkAndApplySnapping(grid, centerCoordinates, currentPosition, nextPosition);
    nextPosition =
        getCoordinates().getPosition()
            .add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));
    setPosition(nextPosition);
  }

  private void checkAndApplySnapping(PacmanGrid grid, Vec2 centerCoordinates, Vec2 currentPosition,
      Vec2 nextPosition) {
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
  }
}
