package ooga.model;

import ooga.util.Vec2;

/**
 * @author George Hong
 */
public class PacMan extends MovingSprite {

  public static final String TYPE = "Pac-Man";
  private final PacmanGrid grid;
  private Vec2 queuedDirection;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed, PacmanGrid grid) {
    super(position, direction, speed);
    queuedDirection = new Vec2(-1, 0);
    this.grid = grid;
  }

  @Override
  public boolean isStationary() {
    return false;
  }

  @Override
  public String getType() {
    return null;
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public void step(double dt) {
    Vec2 userDirection = getInputSource().getRequestedDirection();
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }

    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getExactCoordinates();
    Vec2 nextPosition = currentPosition.add(getDirection().scalarMult(getSpeed()).scalarMult(dt));

    if (centerCoordinates.isBetween(currentPosition, nextPosition)) {
      getCoordinates().setPosition(centerCoordinates);
      TileCoordinates currentTile = getCoordinates().getTileCoordinates();
      // Tile target assuming use of queued direction
      TileCoordinates newTargetTile = new TileCoordinates(
          currentTile.getX() + (int) queuedDirection.getX(),
          currentTile.getY() + (int) queuedDirection.getY());
      // Tile target assuming continued use of current direction
      TileCoordinates currentTargetTile = new TileCoordinates(
          currentTile.getX() + (int) getDirection().getX(),
          currentTile.getY() + (int) getDirection().getY());

      if (grid.getTile(newTargetTile).isOpenToPacman()) {
        setDirection(queuedDirection);
        queuedDirection = null;
      } else if (!grid.getTile(currentTargetTile).isOpenToPacman()) {
        setDirection(Vec2.ZERO);
      }
    }

    nextPosition = getCoordinates().getExactCoordinates()
        .add(getDirection().scalarMult(getSpeed()).scalarMult(dt));
    getCoordinates().setPosition(nextPosition);

  }


  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
