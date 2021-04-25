package ooga.view.audio;

public interface AudioService {

  void playOnce(String soundIdentifier);

  void playIndefinitely(String soundIdentifier);

  void pause(String soundIdentifier);

  void pauseAll();

  void stop(String soundIdentifier);

  void stopAll();
}
