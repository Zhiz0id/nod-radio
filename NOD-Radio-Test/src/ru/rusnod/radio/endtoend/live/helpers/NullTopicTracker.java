package ru.rusnod.radio.endtoend.live.helpers;

import ru.rusnod.radio.live.ui.topics.TopicListener;
import ru.rusnod.radio.live.ui.topics.TopicTracker;

public class NullTopicTracker implements TopicTracker.Factory, TopicTracker {
    @Override
    public TopicTracker create() {
        return this;
    }

    @Override
    public void setListener(TopicListener listener) {

    }
}
