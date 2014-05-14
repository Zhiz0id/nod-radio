package ru.rusnod.radio.live.chat;

public interface ProgressListener {
    void onConnecting();
    void onConnected();
    void onError();
}
