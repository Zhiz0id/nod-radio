package org.dandelion.radiot.endtoend.podcasts;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import org.dandelion.radiot.R;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class PodcastListDriver extends Solo {
    public PodcastListDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void podcastList(Matcher<? super ListView> matcher) {
        assertThat(waitForView(ListView.class), is(true));
        ListView view = getCurrentListViews().get(0);
        assertThat(view, matcher);
    }

    public void firstItemShows(String number, String date, String notes) {
        podcastList(hasFirstItemAs(itemWith(number, date, notes)));
    }

    public Matcher<View> itemWith(String number, String date, String notes) {
        return allOf(
                textField(R.id.podcast_item_view_number, equalTo(number)),
                textField(R.id.podcast_item_view_date, equalTo(date)),
                textField(R.id.podcast_item_view_shownotes, equalTo(notes)));
    }

    public Matcher<? super ListView> hasFirstItemAs(Matcher<View> matcher) {
        return new FeatureMatcher<ListView, View>(matcher, "first list item",
                "first list item") {
            @Override
            protected View featureValueOf(ListView listView) {
                return listView.getChildAt(0);
            }
        };
    }

    private Matcher<? super View> textField(final int id, Matcher<String> matcher) {
        return new FeatureMatcher<View, String>(matcher, "Podcast field", "Podcast field") {
            @Override
            protected String featureValueOf(View container) {
                try {
                    TextView view = (TextView) container.findViewById(id);
                    return (String) view.getText();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}