package org.dandelion.radiot;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class PodcastListActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setPodcastList(samplePodcastList());
	}

	private PodcastItem[] samplePodcastList() {
		return new PodcastItem[] {
				new PodcastItem("#121"),
				new PodcastItem("#122"),
				new PodcastItem("#123")
		};
	}

	public void setPodcastList(PodcastItem[] podcastItems) {
		ListAdapter listAdaptor = new ArrayAdapter<PodcastItem>(this, 
		R.layout.podcast_list_item, R.id.podcast_item_view_number, podcastItems);
		
		setListAdapter(listAdaptor);
	}
}
