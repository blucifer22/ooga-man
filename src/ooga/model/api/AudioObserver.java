package ooga.model.api;

public interface AudioObserver {
    void onPlayOnce(String soundIdentifier);

    void onPlayIndefinitely(String soundIdentifier);

    void onPause(String soundIdentifier);

    void onPauseAll();

    void onStop(String soundIdentifier);

    void onStopAll();
}
