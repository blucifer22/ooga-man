package ooga.model.powerups;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory which creates power-ups.
 */
public class PowerUpFactory {

  private static final List<PowerUp> powerUpList =
      List.of(
          new FrightenPowerUp()
          //      new SpeedUpPowerUp(),
          //      new PointBonusPowerUp(),
          //      new GhostSlowdownPowerUp()
          );

  /**
   * Retrieve a random power-up.
   * @return Random power-up.
   */
  public PowerUp getRandomPowerUp() {
    int dex = ThreadLocalRandom.current().nextInt(powerUpList.size());
    return powerUpList.get(dex);
  }
}
