package ru.rusnod.radio.live.core;

public interface Timeout {
    void reset();
    void set(int milliseconds);
}
