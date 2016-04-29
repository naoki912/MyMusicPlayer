package wcdi.mymusicplayer.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wcdi.common.widget.GenericAdapter;
import wcdi.mymusicplayer.R;
import wcdi.mymusicplayer.item.Song;

public class SongAdapter extends GenericAdapter<Song> {

    public SongAdapter(Context context, int resource) {
        super(context, resource);
    }

    private ViewHolder mViewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Song song = getItem(position);

        if (convertView == null) {

            convertView = super.getView(position, convertView, parent);

            mViewHolder = new ViewHolder();

            mViewHolder.track = (TextView) convertView.findViewById(R.id.fragment_song_track);
            mViewHolder.title = (TextView) convertView.findViewById(R.id.fragment_song_title);
            mViewHolder.time = (TextView) convertView.findViewById(R.id.fragment_song_time);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // この書き方だと、多分track番号が100の音楽ファイルがトラック0になってしまうので対応策を考える
        if (song.mTrack != null) {
            String time = String.valueOf(Long.valueOf(song.mTrack) % 100);
            mViewHolder.track.setText(time);
        } else {
            mViewHolder.track.setText("0");
        }

        mViewHolder.title.setText(song.mTitle);

        // もう少しきれいに書ける気がする
        Long time = getItem(position).mTime / 1000;
        Long minute = time / 60;
        Long second = time % 60;
        mViewHolder.time.setText(String.format("%d:%02d", minute, second));

        return convertView;
    }

    static class ViewHolder {
        TextView track;
        TextView title;
        TextView time;
    }
}
