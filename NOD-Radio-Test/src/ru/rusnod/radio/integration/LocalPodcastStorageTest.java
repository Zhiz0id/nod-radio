package ru.rusnod.radio.integration;

import ru.rusnod.radio.integration.helpers.FileUtils;
import ru.rusnod.radio.podcasts.core.PodcastItem;
import ru.rusnod.radio.podcasts.loader.PodcastsCache;
import ru.rusnod.radio.podcasts.loader.ThumbnailCache;
import ru.rusnod.radio.podcasts.loader.caching.LocalPodcastStorage;
import ru.rusnod.radio.integration.helpers.FileUtils;

import static ru.rusnod.radio.util.PodcastDataBuilder.*;

public class LocalPodcastStorageTest extends FilesystemTestCase {
    private LocalPodcastStorage storage;

    public void testWhenSavingPodcasts_RemovesRedundantThumbnails() throws Exception {
        final String url = "/thumbnail.url";
        final PodcastItem item = aPodcastItem(withThumbnailUrl(url));
        final byte[] thumbnail = new byte[0];

        PodcastsCache podcastsCache = storage.podcastsCache();
        ThumbnailCache thumbnailCache = storage.thumbnailsCache();

        podcastsCache.updateWith(aListWith(item));
        thumbnailCache.update(url, thumbnail);
        assertNotNull(thumbnailCache.lookup(url));

        podcastsCache.updateWith(anEmptyList());
        assertNull(thumbnailCache.lookup(url));
    }

    public void setUp() throws Exception {
        super.setUp();
        FileUtils.mkdir(workDir);
        storage = new LocalPodcastStorage("test-show", workDir);
    }
}
