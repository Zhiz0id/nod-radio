package ru.rusnod.radio.podcasts.ui;

import ru.rusnod.radio.podcasts.loader.PodcastListClient;

public interface PodcastClientFactory {
    PodcastListClient newClientForShow(String name);
}
