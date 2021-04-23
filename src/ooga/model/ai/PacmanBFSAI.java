package ooga.model.ai;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import ooga.model.PacmanGrid;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Add Pac-Man AI that uses a BFS algorithm
 */
public class PacmanBFSAI extends PacmanAI {

  public PacmanBFSAI(PacmanGrid grid, Sprite pacMan) {
    super(grid, pacMan);
  }

  public int getDistanceBFS(TileCoordinates start, TileCoordinates target) {
    Vec2[] directions = {
        new Vec2(-1, 0),
        new Vec2(1, 0),
        new Vec2(0, 1),
        new Vec2(0, -1)
    };
    Set<TileCoordinates> visited = new HashSet<>();
    Queue<TileCoordinates> qu = new LinkedList<>();
    qu.add(start);
    int distance = 0;
    while (!qu.isEmpty()) {
      int levelSize = qu.size();
      for (int k = 0; k < levelSize; k++) {
        TileCoordinates current = qu.remove();
        if (visited.contains(current)) {
          continue;
        }
        visited.add(current);
        if (current.equals(target)) {
          return distance;
        }
        for (Vec2 direction : directions) {
          Vec2 adjPosition = current.toVec2().add(direction);
          TileCoordinates coord = new TileCoordinates(adjPosition);
          if (getPacmanGrid().inBoundaries(coord) && isOpenToPacman(
              adjPosition)) {
            qu.add(coord);
          }
        }
      }
      distance++;
    }
    return Integer.MAX_VALUE;
  }

  @Override
  protected double getCandDistance(Vec2 start, Vec2 end) {
    return getDistanceBFS(new TileCoordinates(start), new TileCoordinates(end));
  }
}
