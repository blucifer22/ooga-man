package ooga.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import ooga.model.api.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.grid.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;
import ooga.model.sprites.TeleporterOverlay;

/**
 * This class contains methods that handle the attachment of all related entities to Sprites. This
 * means the attachment of targets to AIs, teleporters to other teleporters, etc.
 *
 * @author Marc Chmielewski
 * @author George Hong
 */
public class SpriteLinkageFactory {

  private final List<Sprite> ghostList;
  private final PacmanGameState pgs;
  private final HumanInputManager player1;
  private final HumanInputManager player2;
  private Sprite pacman;

  /**
   * This constructor constructs a new SpriteLinkageFactory that will handle the association of
   * Sprites with their necessary related entities.
   *
   * @param pgs The PacmanGameState unto which to complete the linking operation.
   * @param player1 The HumanInputManager for Player 1
   * @param player2 The HumanInputManager for Player 2
   */
  public SpriteLinkageFactory(
      PacmanGameState pgs, HumanInputManager player1, HumanInputManager player2) {
    this.pgs = pgs;
    this.player1 = player1;
    this.player2 = player2;
    ghostList = new ArrayList<>();
    pacman = null;
    for (Sprite sprite : pgs.getSprites()) {
      constructInputSource(pgs, sprite);
      if (sprite.getSwapClass() == SwapClass.PACMAN) {
        pacman = sprite;
      } else if (sprite.getSwapClass() == SwapClass.GHOST) {
        ghostList.add(sprite);
      }
    }
  }

  /**
   * This method will handle the linking of the Sprites in a given PacmanGameState to their
   * respective related entities. This means that Sprites that need InputSources will be linked to
   * their InputSources, and Sprites that need targets linked will have their targets linked here.
   */
  public void linkSprites() {
    attachGhostTargets();
    attachPacmanTargets();
    attachTeleporters(pgs.getSprites());
  }

  private void attachGhostTargets() {
    for (Sprite ghost : ghostList) {
      ghost.getInputSource().addTarget(pacman);
    }
  }

  private void attachPacmanTargets() {
    for (Sprite ghost : ghostList) {
      pacman.getInputSource().addTarget(ghost);
    }
  }

  private void attachTeleporters(List<Sprite> sprites) {
    Map<String, List<TeleporterOverlay>> teleporters = new HashMap<>();
    for (Sprite sprite : sprites) {
      if (sprite.getInputString().toUpperCase(Locale.ROOT).contains("TELEPORTER")) {
        teleporters.putIfAbsent(sprite.getInputString(), new ArrayList<>());
        teleporters.get(sprite.getInputString()).add((TeleporterOverlay) sprite);
      }
    }

    for (String teleporterKey : teleporters.keySet()) {
      TeleporterOverlay teleporterA = teleporters.get(teleporterKey).get(0);
      TeleporterOverlay teleporterB = teleporters.get(teleporterKey).get(1);
      teleporterA.connectTeleporter(teleporterB);
      teleporterB.connectTeleporter(teleporterA);
    }
  }

  private void constructInputSource(PacmanGameState pgs, Sprite sprite) {
    switch (sprite.getInputString()) {
      case "NONE":
        break;
      case "PLAYER_1":
        sprite.setInputSource(player1);
        break;
      case "PLAYER_2":
        sprite.setInputSource(player2);
        break;
      default:
        if (!sprite.getInputString().contains("TELEPORTER")) {
          try {
            Class<?> spriteClass = Class.forName("ooga.model.ai." + sprite.getInputString());
            InputSource spriteInput =
                (InputSource)
                    spriteClass
                        .getDeclaredConstructor(PacmanGrid.class, Sprite.class)
                        .newInstance(pgs.getGrid(), sprite);
            sprite.setInputSource(spriteInput);
          } catch (ClassNotFoundException
              | NoSuchMethodException
              | InstantiationException
              | InvocationTargetException
              | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
          }
        }
        break;
    }
  }
}
