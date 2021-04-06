package ooga.model.sprites;

import ooga.model.PacmanGrid;
import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.util.Vec2;

/** @author George Hong */
public class PacMan extends Sprite {

  public static final String TYPE = "pacman";
  private Vec2 queuedDirection;

  public PacMan(SpriteCoordinates position, Vec2 direction, double speed) {
    super(position, direction, speed);
    queuedDirection = new Vec2(0, 0);
  }

  public PacMan(SpriteDescription spriteDescription) {
    super(spriteDescription);
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public SpriteCoordinates getCenter() {
    return getCoordinates();
  }

  @Override
  public void step(double dt, PacmanGrid grid) {
    Vec2 userDirection = getInputSource().getRequestedDirection();
    if (getDirection().parallelTo(userDirection)) {
      setDirection(userDirection);
      // queuedDirection = userDirection;
    } else if (!userDirection.equals(Vec2.ZERO)) {
      queuedDirection = userDirection;
    }

    Vec2 centerCoordinates = getCoordinates().getTileCenter();
    Vec2 currentPosition = getCoordinates().getPosition();
    Vec2 nextPosition = currentPosition.add(getDirection().scalarMult(getSpeed()).scalarMult(dt));

    // Grid-snapping
    if (centerCoordinates.isBetween(currentPosition, nextPosition)) {
      getCoordinates().setPosition(centerCoordinates);
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

      if (queuedDirection != null && grid.getTile(newTargetTile).isOpenToPacman()) {
        setDirection(queuedDirection);
        queuedDirection = null;
      } else if (!grid.getTile(currentTargetTile).isOpenToPacman()) {
        setDirection(Vec2.ZERO);
      }
    }

    nextPosition =
        getCoordinates().getPosition().add(getDirection().scalarMult(getSpeed()).scalarMult(dt));
    getCoordinates().setPosition(nextPosition);
    notifyObservers();
  }

  @Override
  public boolean mustBeConsumed() {
    return false;
  }
}
