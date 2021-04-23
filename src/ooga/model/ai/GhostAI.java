package ooga.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;
import ooga.model.InputSource;
import ooga.model.PacmanGrid;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * First-order implementation of ghost AI.
 *
 * @author Franklin Wei
 * @author Matthew Belissary
 * @author George Hong
 */
public class GhostAI implements InputSource {

  private final Ghost ghost;
  private final PacmanGrid pacmanGrid;
  private final Map<GhostBehavior, Supplier<Vec2>> movementOptions = new HashMap<>();
  private Sprite target;

  public GhostAI(PacmanGrid grid, Sprite ghost) {
    this.pacmanGrid = grid;
    this.ghost = (Ghost) ghost;
    this.target = null;
    movementOptions.put(GhostBehavior.CHASE, this::chaseBehavior);
    movementOptions.put(GhostBehavior.WAIT, this::waitBehavior);
    movementOptions.put(GhostBehavior.EATEN, this::eatenBehavior);
    movementOptions.put(GhostBehavior.RUNAWAY, this::runawayBehavior);
    movementOptions.put(GhostBehavior.SCATTER, this::scatterBehavior);
  }

  protected Sprite getTarget() {
    return target;
  }

  public void setTarget(Sprite target) {
    this.target = target;
  }

  protected Ghost getGhost() {
    return ghost;
  }

  protected PacmanGrid getPacmanGrid() {
    return pacmanGrid;
  }

  @Override
  public Vec2 getRequestedDirection() {
    if (target == null) {
      throw new IllegalArgumentException("AI has no Target");
    }
    Supplier<Vec2> getAI = movementOptions.get(getGhost().getGhostBehavior());
    return getAI.get();
  }

  protected Vec2 waitBehavior() {
    getGhost().setCurrentSpeed(0);
    return Vec2.ZERO;
  }

  /**
   * This mode corresponds to the ghost seeking home
   *
   * @return
   */
  protected Vec2 eatenBehavior() {
    // TODO: Implement
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();
    Vec2 homeTilePos = getGhost().getSpawn().getTileCoordinates().toVec2();

    return reduceDistance(homeTilePos, currentTilePos);
  }

  /**
   * This mode coincides with the ghost reaction to upon the Power-pill's consumption.
   *
   * @return
   */
  protected Vec2 runawayBehavior() {
    // TODO: Implement
    return scatterBehavior();
  }

  /**
   * This mode coincides with classic Pac-Man where ghosts periodically give up the chase and choose
   * to wander around for a few seconds. This is emulated by default by using a random direction
   * generator.
   *
   * @return direction to queue for ghost to move to
   */
  protected Vec2 scatterBehavior() {
    double scatterProbability = 0.9;
    Vec2 ret = Vec2.ZERO;
    ArrayList<Vec2> randomVectorOptions = new ArrayList<>();
    randomVectorOptions.add(new Vec2(-1.0, 0));
    randomVectorOptions.add(new Vec2(1.0, 0));
    randomVectorOptions.add(new Vec2(0.0, 1.0));
    randomVectorOptions.add(new Vec2(0.0, -1.0));
    Random random = new Random();
    if (random.nextDouble() <= scatterProbability) {
      Random randomVector = new Random();
      ret = randomVectorOptions.get(randomVector.nextInt(randomVectorOptions.size()));
    }
    Vec2 currentReverseDirection = ghost.getDirection().scalarMult(-1);
    return ret.equals(currentReverseDirection) ? Vec2.ZERO : ret;
  }

  /**
   * This behavior can be overridden to define the most aggressive modes for Pac-Man. Targeting can
   * be used here to follow a tracked Sprite, such as Pac-Man. The default GhostAI defaults to
   * random behavior to "track" Pac-Man.
   *
   * @return direction to queue for ghost to move to
   */
  protected Vec2 chaseBehavior() {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }

  private boolean isOpenToGhosts(Vec2 target) {
    if (!pacmanGrid.inBoundaries(new TileCoordinates(target))) {
      return false;
    }
    return pacmanGrid.getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToGhosts();
  }

  @NotNull
  protected Vec2 reduceDistance(Vec2 targetTilePos, Vec2 currentTilePos) {
    Vec2[] directions = {new Vec2(-1, 0), new Vec2(1, 0), new Vec2(0, 1), new Vec2(0, -1)};

    List<DirectionDistanceWrapper> distances = new ArrayList<>();
    for (Vec2 direction : directions) {
      Vec2 nextPos = currentTilePos.add(direction);
      distances.add(new DirectionDistanceWrapper(direction, nextPos.distance(targetTilePos)));
    }
    Collections.sort(distances);
    Vec2 currentReverseDirection = getGhost().getDirection().scalarMult(-1);
    Queue<DirectionDistanceWrapper> queue = new LinkedList<>(distances);

    while (!queue.isEmpty()) {
      Vec2 cand = queue.remove().getVec();
      Vec2 target = currentTilePos.add(cand);
      if (!isOpenToGhosts(target) || cand.equals(currentReverseDirection)) {
        continue;
      }
      return cand;
    }
    return Vec2.ZERO;
  }

  @Override
  public boolean isActionPressed() {
    // TODO: Implement this!
    return false;
  }

  /**
   * Adds a Sprite target to the InputSource.
   *
   * @param target The Sprite to add to the InputSource.
   */
  @Override
  public void addTarget(Sprite target) {
    setTarget(target);
  }
}
