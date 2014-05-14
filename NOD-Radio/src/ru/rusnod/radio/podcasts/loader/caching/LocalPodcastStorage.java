package ru.rusnod.radio.podcasts.loader.caching;

import ru.rusnod.radio.podcasts.core.PodcastList;
import ru.rusnod.radio.podcasts.loader.PodcastsCache;
import ru.rusnod.radio.podcasts.loader.ThumbnailCache;

import java.io.File;

public class LocalPodcastStorage implements FilePodcastsCache.Listener {
    private static final int CACHE_FORMAT_VERSION = 1;

    private FilePodcastsCache podcastsCache;
    private FileThumbnailCache thumbnailsCache;

    public LocalPodcastStorage(String showName, File baseCacheDir) {
        File cacheDir = new File(baseCacheDir, showName);
        CacheFile podcastsFile = new CacheFile(cacheDir, "podcasts.dat");
        CacheDirectory thumbnailsDir = new CacheDirectory(cacheDir, "thumbnails");

        thumbnailsCache = new FileThumbnailCache(thumbnailsDir);
        podcastsCache = new FilePodcastsCache(podcastsFile, CACHE_FORMAT_VERSION);
        podcastsCache.setListener(this);
    }

    public PodcastsCache podcastsCache() {
        return podcastsCache;
    }

    public ThumbnailCache thumbnailsCache() {
        return thumbnailsCache;
    }

    @Override
    public void onUpdatedWith(PodcastList list) {
        thumbnailsCache.cleanup(list.collectThumbnails());
    }
}
