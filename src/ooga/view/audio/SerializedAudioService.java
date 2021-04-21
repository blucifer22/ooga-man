package ooga.view.audio;

import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ooga.view.theme.api.ThemeService;

public class SerializedAudioService implements AudioService {

  private final ThemeService dataSource;
  private final HashMap<String, HashSet<MediaPlayer>> activeAudio;

  public SerializedAudioService(ThemeService dataSource) {
    this.dataSource = dataSource;
    this.activeAudio = new HashMap<>();
  }

  @Override
  public void playOnce(String soundIdentifier) {
    activeAudio.putIfAbsent(soundIdentifier, new HashSet<>());
    Media singlePlayAudio = dataSource.getTheme().getSoundFromIdentifier(soundIdentifier);
    MediaPlayer spaPlayer = new MediaPlayer(singlePlayAudio);
    activeAudio.get(soundIdentifier).add(spaPlayer);
    spaPlayer.setOnEndOfMedia(() -> {
      activeAudio.get(soundIdentifier).remove(spaPlayer);
    });
    spaPlayer.play();
  }

  @Override
  public void playIndefinitely(String soundIdentifier) {
    activeAudio.putIfAbsent(soundIdentifier, new HashSet<>());
    Media singlePlayAudio = dataSource.getTheme().getSoundFromIdentifier(soundIdentifier);
    MediaPlayer spaPlayer = new MediaPlayer(singlePlayAudio);
    activeAudio.get(soundIdentifier).add(spaPlayer);
    spaPlayer.setOnEndOfMedia(() -> {
      spaPlayer.seek(Duration.ZERO);
      spaPlayer.play();
    });
    spaPlayer.play();
  }

  @Override
  public void pause(String soundIdentifier) {
    if (activeAudio.containsKey(soundIdentifier)) {
      for (MediaPlayer player: activeAudio.get(soundIdentifier)) {
        player.pause();
      }
    }
  }

  @Override
  public void pauseAll() {
    for (HashSet<MediaPlayer> soundPlayers : activeAudio.values()) {
      for (MediaPlayer player: soundPlayers) {
        player.pause();
      }
    }
  }

  @Override
  public void stop(String soundIdentifier) {
    if (activeAudio.containsKey(soundIdentifier)) {
      for (MediaPlayer player: activeAudio.get(soundIdentifier)) {
        player.stop();
      }
      activeAudio.get(soundIdentifier).clear();
    }
  }

  @Override
  public void stopAll() {
    for (HashSet<MediaPlayer> soundPlayers : activeAudio.values()) {
      for (MediaPlayer player: soundPlayers) {
        player.pause();
      }
      soundPlayers.clear();
    }
  }
}
