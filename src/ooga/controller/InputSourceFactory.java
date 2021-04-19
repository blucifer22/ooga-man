package ooga.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import ooga.model.InputSource;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGrid;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * This class contains methods that handle the attachment of targets to InputSources.
 *
 * @author Marc Chmielewski
 * @author George Hong
 */
public class InputSourceFactory {

  private final List<Sprite> ghostList;
  private Sprite pacman;
  private HumanInputManager player1;
  private HumanInputManager player2;

  public InputSourceFactory(PacmanGameState pgs, HumanInputManager player1, HumanInputManager player2) {
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
    attachGhostTargets();
    attachPacmanTargets();
  }

  private void attachGhostTargets() {
    for (Sprite ghost : ghostList) {
      System.out.println(ghost + " " + ghost.getInputSource());
      ghost.getInputSource().addTarget(pacman);
    }
  }

  private void attachPacmanTargets() {
    for (Sprite ghost : ghostList) {
      pacman.getInputSource().addTarget(ghost);
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
        try {
          Class<?> spriteClass = Class.forName("ooga.model.ai." + sprite.getInputString());
          InputSource spriteInput =
              (InputSource)
                  spriteClass
                      .getDeclaredConstructor(PacmanGrid.class, Sprite.class)
                      .newInstance(pgs.getGrid(), sprite);
          System.out.println("Sprite Input: " + spriteInput);
          sprite.setInputSource(spriteInput);
        } catch (ClassNotFoundException
            | NoSuchMethodException
            | InstantiationException
            | InvocationTargetException
            | IllegalAccessException e) {
          e.printStackTrace();
          System.err.println(e.getMessage());
        }
        break;
    }
  }
}
