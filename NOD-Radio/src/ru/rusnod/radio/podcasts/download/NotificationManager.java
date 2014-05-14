package ru.rusnod.radio.podcasts.download;

import java.io.File;

public interface NotificationManager {
    void showSuccess(String title, File audioFile);
    void showError(String title, int errorCode);
}
