package ru.rusnod.radio.podcasts.core;

import android.content.Context;

public interface PodcastAction {
	void perform(Context context, PodcastItem podcast);
}
