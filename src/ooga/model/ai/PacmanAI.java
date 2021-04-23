package ooga.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import ooga.model.InputSource;
import ooga.model.PacmanGrid;
import ooga.model.TileCoordinates;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * Basic AI for Pac-Man that focuses on maximizing distance between all included target Sprites
 *
 * @author George Hong
 */
public class PacmanAI implements InputSource {

  private final List<Sprite> targets;
  private final PacmanGrid pacmanGrid;
  private final Sprite pacMan;

  public PacmanAI(PacmanGrid grid, Sprite pacMan) {
    this.pacMan = pacMan;
    targets = new ArrayList<>();
    pacmanGrid = grid;
  }

  protected PacmanGrid getPacmanGrid() {
    return pacmanGrid;
  }

  protected List<Sprite> getTargets() {
    return targets;
  }

  protected Sprite getPacMan() {
    return pacMan;
  }

  /**
   * Add Sprite to be tracked by the movement algorithm.  The algorithm will try to maximize the
   * distance between Pac-Man and these sprites.
   *
   * @param sprite Sprites to move away from.
   */
  public void addTarget(Sprite sprite) {
    targets.add(sprite);
  }

  /**
   * The Pac-Man AI is focused on survival.  It considers the position of all ghosts in the game.
   * For each direction that it can choose, it considers the closest Sprite in that direction to
   * make decisions.  The final direction chosen is an attempt to maximize the closest distance.
   * This AI can freely reverse, if necessary.
   *
   * @param currentTilePos current position of this AI
   * @return Direction to send to the Sprite
   */
  protected Vec2 maximizeDistance(Vec2 currentTilePos) {
    Vec2[] directions = {
        new Vec2(0, 0),
        new Vec2(-1, 0),
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, -1)
    };

    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      double closestDistance = Double.MAX_VALUE;
      for (Sprite target : targets) {
        Vec2 targetTilePos = target.getCoordinates().getTileCoordinates().toVec2();
//        double candDistance = getDistanceBFS(new TileCoordinates(nextPos),
//            new TileCoordinates(targetTilePos));
        double candDistance = getCandDistance(nextPos,targetTilePos);
        closestDistance = Math.min(candDistance, closestDistance);
      }
      distances.add(new DirectionDistanceWrapper(direction, closestDistance));
    }

    Collections.sort(distances, Collections.reverseOrder());
    //Vec2 currentReverseDirection = getGhost().getDirection().scalarMult(-1);
    Queue<DirectionDistanceWrapper> queue = new LinkedList<>(distances);

    while (!queue.isEmpty()) {
      Vec2 cand = queue.remove().getVec();
      Vec2 target = currentTilePos.add(cand);
      if (!isOpenToPacman(target)) {
        continue;
      }
      return cand;
    }
    return Vec2.ZERO;
  }

  protected double getCandDistance(Vec2 start, Vec2 end) {
    return end.distance(start);
  }

  protected boolean isOpenToPacman(Vec2 target) {
    return pacmanGrid.getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToGhosts();
  }

  @Override
  public Vec2 getRequestedDirection() {
    Vec2 currentTilePos = pacMan.getCoordinates().getTileCoordinates().toVec2();
    return maximizeDistance(currentTilePos);
  }

  @Override
  public boolean isActionPressed() {
    return false;
  }

}
