package ooga.model.sprites;

import java.util.Map;
import ooga.model.GameEvent;
import ooga.model.MutableGameState;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.Tile;
import ooga.model.grid.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/** @author George Hong */
public abstract class MoveableSprite extends Sprite {

  /**
   * Maximum movement speed cap. 9 u/s.
   */
  public static final int UNIVERSAL_MAX_MOVEMENT_SPEED = 9;
  private double currentSpeed;
  private double movementSpeed;
  private Vec2 queuedDirection;
  private boolean frozen;

  private double initialMovementSpeed;

  /**
   * Construct a moveable sprite.
   * @param spriteAnimationPrefix Animation prefix.
   * @param startingAnimation Starting animation.
   * @param position Initial position.
   * @param direction Initial orientation.
   * @param speed Movement speed.
   */
  protected MoveableSprite(
      String spriteAnimationPrefix,
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

  /**
   * Construct a moveable sprite from a sprite description.
   * @param spriteAnimationPrefix Animation prefix.
   * @param startingAnimation Starting animation.
   * @param description Sprite description.
   */
  protected MoveableSprite(
      String spriteAnimationPrefix,
      SpriteAnimationFactory.SpriteAnimationType startingAnimation,
      SpriteDescription description) {
    super(spriteAnimationPrefix, startingAnimation, description);
  }

  /**
   * Called on a respawn. Resets movement speed.
   */
  @Override
  public void reset() {
    super.reset();
    movementSpeed = initialMovementSpeed;
  }

  /**
   * Get default movement speed.
   * @return Movement speed.
   */
  public double getMovementSpeed() {
    return movementSpeed;
  }

  /**
   * Set default movement speed.
   * @param speed Speed to set.
   */
  public void setMovementSpeed(double speed) {
    this.movementSpeed = speed;
  }

  /**
   * Get current speed.
   * @return Current speed.
   */
  public double getCurrentSpeed() {
    return currentSpeed;
  }

  /**
   * Set current speed.
   * @param speed Speed to set.
   */
  public void setCurrentSpeed(double speed) {
    this.currentSpeed = speed;
  }

  /**
   * Moveable Sprites can update their speed with every new round to increase the difficulty of the
   * Pac-Man game. Additionally, a maximum movement speed is present to cap Sprites from moving too
   * quickly.
   *
   * @param roundNumber current round of Pac-Man.
   * @param state Game state.
   */
  @Override
  public void uponNewLevel(int roundNumber, MutableGameState state) {
    movementSpeed =
        Math.min(initialMovementSpeed + 0.5 * roundNumber, UNIVERSAL_MAX_MOVEMENT_SPEED);
    frozen = true;
  }

  /**
   * Check whether this sprite can move into a tile.
   * @param tile Destination.
   * @return True if can move into.
   */
  protected abstract boolean canMoveTo(Tile tile);

  /**
   * Unfreeze this sprite. Called after the start music plays.
   */
  public void unfreeze() {
    frozen = false;
  }

  /**
   * Freeze this sprite. Prevents sprite from moving.
   */
  public void freeze() {
    frozen = true;
  }

  /**
   * Move this sprite according to its input source.
   * @param dt Time step.
   * @param grid Grid on which this sprite moves.
   */
  public void move(double dt, PacmanGrid grid) {
    if (frozen) {
      return;
    }
    Vec2 userDirection = getInputSource().getRequestedDirection(dt);
    userDirection = userDirection.getMagnitude() == 1 ? userDirection : Vec2.ZERO;
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
      currentSpeed = movementSpeed;
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }
    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getPosition();
    Vec2 nextPosition =
        currentPosition.add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));
    // Grid-snapping
    checkAndApplySnapping(grid, centerCoordinates, currentPosition, nextPosition);
    nextPosition =
        getCoordinates()
            .getPosition()
            .add(getDirection().scalarMult(getCurrentSpeed()).scalarMult(dt));
    setPosition(nextPosition);
  }

  private void checkAndApplySnapping(
      PacmanGrid grid, Vec2 centerCoordinates, Vec2 currentPosition, Vec2 nextPosition) {
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
