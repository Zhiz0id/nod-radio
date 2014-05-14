package ru.rusnod.radio.podcasts.loader;

import ru.rusnod.radio.podcasts.core.PodcastList;

public interface PodcastsCache {
    boolean hasValidData();
    PodcastList getData();
    void updateWith(PodcastList data);
}
