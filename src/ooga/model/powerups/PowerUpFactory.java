package ooga.model.powerups;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PowerUpFactory {

  private static final List<PowerUp> powerUpList = List.of(
      new FrightenPowerUp()
  );

  public PowerUp getRandomPowerUp() {
    int dex = ThreadLocalRandom.current().nextInt(powerUpList.size());
    return powerUpList.get(dex);
  }
}
