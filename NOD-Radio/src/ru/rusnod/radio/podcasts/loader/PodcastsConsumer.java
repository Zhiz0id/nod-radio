package ru.rusnod.radio.podcasts.loader;

import ru.rusnod.radio.podcasts.core.PodcastItem;
import ru.rusnod.radio.podcasts.core.PodcastList;

public interface PodcastsConsumer {
    void updateList(PodcastList podcasts);
    void updateThumbnail(PodcastItem item, byte[] thumbnail);

    public static PodcastsConsumer Null = new PodcastsConsumer() {
        @Override
        public void updateList(PodcastList podcasts) {
        }

        @Override
        public void updateThumbnail(PodcastItem item, byte[] thumbnail) {
        }
    };
}
