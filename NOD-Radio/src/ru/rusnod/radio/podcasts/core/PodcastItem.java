package ru.rusnod.radio.podcasts.core;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastItem implements Cloneable, Serializable {
    public static final Pattern THUMBNAIL_URL_PATTERN = Pattern.compile("<img.+src=\"(\\S+)\".*/?>");

    public String pubDate = "";
	public String showNotes = "";
	public String audioUri = "";
    public String title = "";
    public String thumbnailUrl = null;

    public void extractThumbnailUrl(String description) {
        Matcher matcher = THUMBNAIL_URL_PATTERN.matcher(description);
        if (matcher.find()) {
            thumbnailUrl = matcher.group(1);
        }
    }

    public boolean hasThumbnail() {
        return thumbnailUrl != null;
    }
}
