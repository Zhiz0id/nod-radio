package org.dandelion.radiot.live;

import java.util.List;

import org.dandelion.radiot.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LiveShowTopics extends LinearLayout {
	private LiveShowTopicsPresenter presenter;
	private ArrayAdapter<ShowTopic> listAdapter;

	public LiveShowTopics(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout();
		initListView();

		presenter = new LiveShowTopicsPresenter(this);
	}

	private void initListView() {
		ListView view = (ListView) findViewById(R.id.live_topics_list);
		listAdapter = new ArrayAdapter<ShowTopic>(getContext(),
				android.R.layout.simple_list_item_1);
		view.setAdapter(listAdapter);
	}

	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.live_show_topics, this);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		presenter.refreshTopics();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		presenter.cancelAll();
	}

	public void setTopics(List<ShowTopic> topics) {
		listAdapter.clear();
		for (ShowTopic topic : topics) {
			listAdapter.add(topic);
		}
	}
}
