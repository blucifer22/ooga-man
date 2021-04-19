package ooga.controller;

import java.util.ArrayList;
import java.util.List;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * This class contains methods that handle the attachment of targets to InputSources.
 *
 * @author Marc Chmielewski
 * @author George Hong
 */
public class InputSourceFactory {

  List<Sprite> ghostList;
  Sprite pacman;

  public InputSourceFactory(List<Sprite> spriteList) {
    ghostList = new ArrayList<>();
    pacman = null;
    for (Sprite sprite : spriteList) {
      if (sprite.getSwapClass() == SwapClass.PACMAN) {
        pacman = sprite;
      } else if (sprite.getSwapClass() == SwapClass.GHOST) {
        ghostList.add(sprite);
      }
    }
  }

  public void attachGhostTargets() {
    for (Sprite ghost : ghostList) {
      ghost.getInputSource().addTarget(pacman);
    }
  }

  public void attachPacmanTargets() {
    for (Sprite ghost : ghostList) {
      pacman.getInputSource().addTarget(ghost);
    }
  }
}
