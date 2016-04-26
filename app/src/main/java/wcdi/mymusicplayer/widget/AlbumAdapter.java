package wcdi.mymusicplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import wcdi.common.widget.GenericAdapter;
import wcdi.mymusicplayer.R;
import wcdi.mymusicplayer.item.Album;

public class AlbumAdapter extends GenericAdapter<Album> {

    public AlbumAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;

        mResource = resource;

        mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    private ViewHolder mViewHolder;

    private Context mContext;

    private int mResource;

    private LayoutInflater mLayoutInflater;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Album album = getItem(position);

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(mResource, parent, false);

            mViewHolder = new ViewHolder();

            mViewHolder.album = (TextView) convertView.findViewById(R.id.fragment_album_album);
            mViewHolder.artist = (TextView) convertView.findViewById(R.id.fragment_album_artist) ;
            mViewHolder.albumArt = (ImageView) convertView.findViewById(R.id.fragment_album_album_art);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
            mViewHolder.albumArt.setImageResource(R.drawable.default_album_art);
        }

        mViewHolder.album.setText(album.mAlbum);
        mViewHolder.artist.setText(album.mArtist);

        if (album.mAlbumArt != null) {
            mViewHolder.albumArt.setTag(album.mAlbumArt);

            AlbumArtLoader task = new AlbumArtLoader(mViewHolder.albumArt);
            task.execute(album.mAlbumArt);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView album;
        TextView artist;
        ImageView albumArt;
    }

    public class AlbumArtLoader extends AsyncTask<String, Bitmap, Bitmap> {

        private ImageView imageView;
        private String tag;

        public AlbumArtLoader(ImageView imageView) {
            this.imageView = imageView;
            this.tag = imageView.getTag().toString();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            synchronized (mContext) {
                File file = new File(strings[0]);
                return new BitmapFactory().decodeFile(file.getAbsolutePath());
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (! this.tag.equals(this.imageView.getTag())) {
                return;
            }

            if (result != null && imageView != null) {
                this.imageView.setImageBitmap(result);
            }
        }
    }
}
