package wcdi.mymusicplayer.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wcdi.common.widget.GenericAdapter;
import wcdi.mymusicplayer.R;
import wcdi.mymusicplayer.item.Artist;

public class ArtistAdapter extends GenericAdapter<Artist> {

    /** 仮実装 */

    public ArtistAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Artist artist = getItem(position);

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {

            convertView = super.getView(position, convertView, parent);

            viewHolder.artist = (TextView) convertView.findViewById(R.id.fragment_artist_artist) ;

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artist.setText(artist.mArtist);

        return convertView;
    }

    static class ViewHolder {
        TextView artist;
    }
}
