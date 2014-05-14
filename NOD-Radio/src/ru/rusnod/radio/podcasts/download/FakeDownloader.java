package ru.rusnod.radio.podcasts.download;

import android.content.Context;
import android.content.Intent;
import ru.rusnod.radio.podcasts.core.PodcastAction;
import ru.rusnod.radio.podcasts.core.PodcastItem;

public class FakeDownloader implements PodcastAction {
    @Override
    public void perform(Context context, PodcastItem podcast) {
        context.startActivity(new Intent(context, FakeDownloaderActivity.class));
    }
}
