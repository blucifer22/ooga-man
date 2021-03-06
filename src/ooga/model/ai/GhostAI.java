package ooga.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import ooga.model.api.InputSource;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.TileCoordinates;
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

  private static final double WIGGLE_PERIOD = 0.5; // 2 Hz oscillation
  private final Ghost ghost;
  private final PacmanGrid pacmanGrid;
  private final Map<GhostBehavior, Function<Double, Vec2>> movementOptions = new HashMap<>();
  private Sprite target;
  private double wiggleTime;

  /**
   * Constructs a GhostAI object given a grid and a ghost sprite
   *
   * @param grid Grid on which this AI exists.
   * @param ghost Ghost this AI controls.
   */
  public GhostAI(PacmanGrid grid, Sprite ghost) {
    this.pacmanGrid = grid;
    this.ghost = (Ghost) ghost;
    this.target = null;
    movementOptions.put(GhostBehavior.CHASE, this::chaseBehavior);
    movementOptions.put(GhostBehavior.WAIT, this::waitBehavior);
    movementOptions.put(GhostBehavior.EATEN, this::eatenBehavior);
    movementOptions.put(GhostBehavior.RUNAWAY, this::runawayBehavior);
    movementOptions.put(GhostBehavior.SCATTER, this::scatterBehavior);
    wiggleTime = (0.25 * WIGGLE_PERIOD);
  }

  /**
   * Return target sprite.
   *
   * @return the target of the current AI
   */
  protected Sprite getTarget() {
    return target;
  }

  /**
   * Sets the target sprite for the AI
   *
   * @param target Target sprite
   */
  public void setTarget(Sprite target) {
    this.target = target;
  }

  /**
   * Query ghost this is controlling.
   *
   * @return the Ghost that the AI is  controlling
   */
  protected Ghost getGhost() {
    return ghost;
  }

  /**
   * Query grid.
   *
   * @return the current game grid
   */
  protected PacmanGrid getPacmanGrid() {
    return pacmanGrid;
  }

  /**
   * Provides the new Vec2 that the ghost will travel in depending on the state of the ghost
   *
   * @param dt Time step.
   * @return Requested movement direction.
   */
  @Override
  public Vec2 getRequestedDirection(double dt) {
    if (target == null) {
      throw new IllegalArgumentException("AI has no Target");
    }
    Function<Double, Vec2> getAI = movementOptions.get(getGhost().getGhostBehavior());
    return getAI.apply(dt);
  }

  /**
   * This mode corresponds to the ghost waiting and wiggling until they are able to leave the Ghost box
   *
   * @param dt Time step.
   * @return Movement direction.
   */
  protected Vec2 waitBehavior(double dt) {
    getGhost().setMovementSpeed(ghost.getDefaultMoveSpeed());
    wiggleTime += dt;
    return (int) (wiggleTime / (WIGGLE_PERIOD / 2)) % 2 == 1 ? Vec2.UP : Vec2.DOWN;
  }

  /**
   * This mode corresponds to the ghost seeking their original spawn point
   *
   * @param dt Time step.
   * @return Movement direction.
   */
  protected Vec2 eatenBehavior(double dt) {
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();
    Vec2 homeTilePos = getGhost().getSpawn().getTileCoordinates().toVec2();

    return reduceDistance(homeTilePos, currentTilePos);
  }

  /**
   * This mode coincides with the ghost reaction to upon the Power-pill's consumption.
   *
   * @param dt Time step.
   * @return Movement direction.
   */
  protected Vec2 runawayBehavior(double dt) {
    return scatterBehavior(dt);
  }

  /**
   * This mode coincides with classic Pac-Man where ghosts periodically give up the chase and choose
   * to wander around for a few seconds. This is emulated by default by using a random direction
   * generator.
   *
   * @param dt Time step.
   * @return direction to queue for ghost to move to
   */
  protected Vec2 scatterBehavior(double dt) {
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
   * @param dt Time step.
   * @return direction to queue for ghost to move to
   */
  protected Vec2 chaseBehavior(double dt) {
    Vec2 targetTilePos = getTarget().getCoordinates().getTileCoordinates().toVec2();
    Vec2 currentTilePos = getGhost().getCoordinates().getTileCoordinates().toVec2();

    return reduceDistance(targetTilePos, currentTilePos);
  }

  private boolean isOpenToGhosts(Vec2 target) {
    if (!pacmanGrid.inBoundaries(new TileCoordinates(target))) {
      return false;
    }
    return pacmanGrid
        .getTile(new TileCoordinates((int) target.getX(), (int) target.getY()))
        .isOpenToGhosts();
  }

  /**
   * Finds the Vec2 for the ghost to travel upon that best reduces the distance between the target
   * and the ghost
   *
   * @param targetTilePos Target position.
   * @param currentTilePos Current position.
   * @return Best move.
   */
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

  /**
   * Query action key.
   *
   * @return false since key action events do not change GhostAI
   */
  @Override
  public boolean isActionPressed() {
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
