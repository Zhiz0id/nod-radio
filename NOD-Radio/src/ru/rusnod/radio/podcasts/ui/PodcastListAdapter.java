package ru.rusnod.radio.podcasts.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ru.rusnod.radio.R;
import ru.rusnod.radio.podcasts.core.PodcastItem;
import ru.rusnod.radio.podcasts.core.PodcastList;
import ru.rusnod.radio.podcasts.loader.PodcastsConsumer;

class PodcastListAdapter extends ArrayAdapter<PodcastVisual> implements PodcastsConsumer {
    private LayoutInflater inflater;

    public PodcastListAdapter(Activity activity) {
        super(activity, 0);
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PodcastItemView row = (PodcastItemView) convertView;
        if (row == null) {
            row = (PodcastItemView) inflater.inflate(R.layout.podcast_list_item, parent, false);
        }

        PodcastVisual pv = getItem(position);
        associate(row, pv);
        return row;
    }

    private void associate(PodcastItemView row, PodcastVisual pv) {
        disassoc(row);
        assoc(row, pv);
    }

    private void assoc(PodcastItemView row, PodcastVisual pv) {
        row.setTag(pv);
        pv.associateWith(row);
    }

    private void disassoc(PodcastItemView row) {
        PodcastVisual former = (PodcastVisual) row.getTag();
        if (former != null) {
            former.disassociate();
        }
    }

    @Override
    public void updateList(PodcastList podcasts) {
        clear();
        for (PodcastItem item : podcasts) {
            add(new PodcastVisual(item));
        }
    }

    @Override
    public void updateThumbnail(PodcastItem item, byte[] thumbnail) {
        PodcastVisual pv = findVisualForItem(item);
        pv.updateThumbnail(createDrawable(thumbnail));
    }

    private Drawable createDrawable(byte[] thumbnail) {
        final Resources res = getContext().getResources();

        if (thumbnail != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            if (bitmap != null) {
                return new BitmapDrawable(res, bitmap);
            }
        }
        return null;
    }

    private PodcastVisual findVisualForItem(PodcastItem item) {
        for(int i = 0; i < getCount(); i++) {
            PodcastVisual visual = getItem(i);
            if (visual.podcast == item) {
                return visual;
            }
        }
        return null;
    }
}
