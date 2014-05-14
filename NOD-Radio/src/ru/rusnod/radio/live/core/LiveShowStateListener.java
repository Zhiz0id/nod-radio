package ru.rusnod.radio.live.core;

public interface LiveShowStateListener {
    void onStateChanged(LiveShowState state, long timestamp);
}
