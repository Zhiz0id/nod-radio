package ru.rusnod.radio.podcasts.loader;

import ru.rusnod.radio.podcasts.core.ErrorListener;

public interface ProgressListener extends ErrorListener {
    void onStarted();
    void onFinished();

    public static ProgressListener Null = new ProgressListener() {
        @Override
        public void onStarted() {
        }

        @Override
        public void onFinished() {
        }

        @Override
        public void onError(String errorMessage) {
        }
    };
}
