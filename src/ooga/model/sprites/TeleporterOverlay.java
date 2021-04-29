package ooga.model.sprites;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ooga.model.MutableGameState;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.animation.SpriteAnimationFactory;
import ooga.util.Vec2;

/**
 * The Teleport Overlay is an invisible Sprite that redirects Sprite movement to a parallel
 * TeleportOverlay. Can be taken by any Sprite with movement.
 *
 * @author George Hong
 */
public class TeleporterOverlay extends Sprite {

  private final List<Sprite> connectedTeleporters;

  /**
   * Constructs an instance of a Teleport Overlay
   *
   * @param position position of the teleporter
   */
  public TeleporterOverlay(SpriteCoordinates position) {
    super("", SpriteAnimationFactory.SpriteAnimationType.BLANK, position, Vec2.ZERO);
    connectedTeleporters = new ArrayList<>();
  }

  /**
   * Construct a teleporter from a sprite description, useful for when loading from a configuration
   * file.
   *
   * @param spriteDescription Description to use.
   */
  public TeleporterOverlay(SpriteDescription spriteDescription) {
    this(spriteDescription.getCoordinates());
  }

  /**
   * Adds a teleporter that is parallel to this.  A teleporter can be connected to multiple
   * teleporters
   *
   * @param teleportOverlay Other teleporter.
   */
  public void connectTeleporter(Sprite teleportOverlay) {
    connectedTeleporters.add(teleportOverlay);
  }

  /**
   * Gets this TeleporterOverlay's List of connected Teleporters.
   *
   * @return This TeleporterOverlay's List of connected Teleporters.
   */
  public List<Sprite> getConnectedTeleporters() {
    return connectedTeleporters;
  }

  /**
   * Changes the position of the Sprite that collides with this. This object can not be modified by
   * collision by other Sprites. An object entering this teleporter will leave the teleporter moving
   * in the same direction, which is an important decision in order to avoid rubberbanding or
   * placement into an invalid tile position. The teleporter that the entering Sprite leaves is
   * randomly chosen of the connected teleporters.
   *
   * @param other other Sprite that this sprite collides with
   * @param state current state of the game, allowing Sprites to perform actions such as remove
   */
  @Override
  public void uponHitBy(Sprite other, MutableGameState state) {
    int dex = ThreadLocalRandom.current().nextInt(connectedTeleporters.size());
    Sprite connectedTeleporter = connectedTeleporters.get(dex);
    Vec2 teleporterCenter = connectedTeleporter.getCoordinates().getTileCenter();
    Vec2 spriteDirection = other.getDirection();

    Vec2 newSpritePosition = teleporterCenter.add(spriteDirection.scalarMult(0.51));

    other.setCoordinates(new SpriteCoordinates(newSpritePosition));
  }

  /**
   * Point value of this teleporter.  Teleporters can not be consumed and provide no score.
   *
   * @return 0
   */
  @Override
  public int getScore() {
    return 0;
  }

  /**
   * Consumability of this teleporter.  Teleporters cannot be consumed.
   *
   * @return false
   */
  @Override
  public boolean isConsumable() {
    return false;
  }
}
