package ru.rusnod.radio.endtoend.live;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import ru.rusnod.radio.endtoend.live.helpers.NullChatTranslation;
import ru.rusnod.radio.endtoend.live.helpers.NullTopicTracker;
import ru.rusnod.radio.live.ui.ChatTranslationFragment;
import ru.rusnod.radio.live.ui.LiveShowActivity;
import ru.rusnod.radio.live.ui.topics.CurrentTopicFragment;
import ru.rusnod.radio.endtoend.live.helpers.NullTopicTracker;

public class LiveShowActivityTestCase extends ActivityInstrumentationTestCase2<LiveShowActivity> {
    @TargetApi(Build.VERSION_CODES.FROYO)
    public LiveShowActivityTestCase() {
        super(LiveShowActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CurrentTopicFragment.trackerFactory = new NullTopicTracker();
        ChatTranslationFragment.chatFactory = new NullChatTranslation();
    }
}
