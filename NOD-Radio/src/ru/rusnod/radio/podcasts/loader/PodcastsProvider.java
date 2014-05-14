package ru.rusnod.radio.podcasts.loader;

import ru.rusnod.radio.podcasts.core.PodcastList;

public interface PodcastsProvider {
    PodcastList retrieve() throws Exception;
}
