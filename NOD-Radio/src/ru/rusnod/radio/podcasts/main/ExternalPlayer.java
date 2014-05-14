package ru.rusnod.radio.podcasts.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import ru.rusnod.radio.podcasts.core.PodcastAction;
import ru.rusnod.radio.podcasts.core.PodcastItem;

class ExternalPlayer implements PodcastAction {
    public ExternalPlayer() {
    }

    @Override
    public void perform(Context context, PodcastItem podcast) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(podcast.audioUri), "audio/mpeg");
		context.startActivity(Intent.createChooser(intent, null));
	}
}