package ru.rusnod.radio.live.service;

public interface Lockable {
    void release();

    void acquire();
}
