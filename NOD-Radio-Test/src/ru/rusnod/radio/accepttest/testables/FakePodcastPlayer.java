package ru.rusnod.radio.accepttest.testables;

import android.content.Context;
import junit.framework.Assert;
import ru.rusnod.radio.helpers.SyncValueHolder;
import ru.rusnod.radio.podcasts.core.PodcastItem;
import ru.rusnod.radio.podcasts.core.PodcastAction;
import ru.rusnod.radio.helpers.SyncValueHolder;

public class FakePodcastPlayer implements PodcastAction {
    private SyncValueHolder<String> podcastToPlay = new SyncValueHolder<String>();

	public void perform(Context context, PodcastItem podcast) {
        podcastToPlay.setValue(podcast.audioUri);
	}

	public void assertIsPlaying(String url) throws Exception {
		Assert.assertEquals(url, podcastToPlay.getValue());
	}
}