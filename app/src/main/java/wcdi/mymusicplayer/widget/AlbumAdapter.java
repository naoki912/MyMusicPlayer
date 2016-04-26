package wcdi.mymusicplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

            mViewHolder = new ViewHolder();

            convertView = mLayoutInflater.inflate(mResource, parent, false);

            mViewHolder.album = (TextView) convertView.findViewById(R.id.fragment_album_album);
            mViewHolder.artist = (TextView) convertView.findViewById(R.id.fragment_album_artist) ;
            mViewHolder.albumArt = (ImageView) convertView.findViewById(R.id.fragment_album_album_art);

            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.album.setText(album.mAlbum);
        mViewHolder.artist.setText(album.mArtist);

        // ToDo 以下の処理を、非同期でアルバムアートを取得するように変更
        if (album.mAlbumArt != null) {
            File path = new File(album.mAlbumArt);
            Bitmap bitmap = new BitmapFactory().decodeFile(path.getAbsolutePath());
            mViewHolder.albumArt.setImageBitmap(bitmap);
        } else {
//            mViewHolder.albumArt.setImageBitmap(R.drawable.default_image);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView album;
        TextView artist;
        ImageView albumArt;
    }
}
