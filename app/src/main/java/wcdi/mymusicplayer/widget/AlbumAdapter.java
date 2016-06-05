package wcdi.mymusicplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

import wcdi.common.widget.GenericAdapter;
import wcdi.mymusicplayer.R;
import wcdi.mymusicplayer.item.Album;

public class AlbumAdapter extends GenericAdapter<Album> {

    private static ImageCache mImageCache;

    public AlbumAdapter(Context context, int resource) {
        super(context, resource);

        mImageCache = new ImageCache();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Album album = getItem(position);

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {

            convertView = super.getView(position, convertView, parent);

            viewHolder.album = (TextView) convertView.findViewById(R.id.fragment_album_album);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.fragment_album_artist) ;
            viewHolder.albumArt = (ImageView) convertView.findViewById(R.id.fragment_album_album_art);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            // 一度デフォルトのアルバムアートに戻さないと前回使用したAlbumArtが再表示されてしまう
            viewHolder.albumArt.setImageResource(R.drawable.default_album_art);
        }

        viewHolder.album.setText(album.mAlbum);
        viewHolder.artist.setText(album.mArtist);

        if (album.mAlbumArt != null) {
            viewHolder.albumArt.setTag(album.mAlbumArt);

            AlbumArtLoader task = new AlbumArtLoader(viewHolder.albumArt);
            task.execute(album.mAlbumArt);
        }

        return convertView;
    }

    class ViewHolder {
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
        protected void onPostExecute(Bitmap result) {
            if (! this.tag.equals(this.imageView.getTag())) {
                return;
            }

            if (result != null && imageView != null) {
                this.imageView.setImageBitmap(result);
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            synchronized (getContext()) {

                Bitmap bitmap = mImageCache.getImage(strings[0]);

                if (bitmap == null) {
                    File file = new File(strings[0]);
                    bitmap = new BitmapFactory().decodeFile(file.getAbsolutePath());
                    mImageCache.setImage(strings[0], bitmap);
                }

                return bitmap;
            }
        }
    }

    /**
     * 現状だとリストをすべてキャッシュにしてしまっているが
     * キャッシュする上限をつけてあげないと、アルバムが大量にある際にメモリがヤバイことになりそう
     *
     * 画面遷移した際もずっとキャッシュとして持っておきたい場合はstaticクラスに変更すれば可能
     * もしくは、Fragment自体にキャッシュ用のHashMapを持たせておいて、onSaveInstanceState
     * あたりで使い回す
     */
    public class ImageCache {

        private HashMap<String, Bitmap> cache = new HashMap<>();

        public Bitmap getImage(String key) {
            if (cache.containsKey(key)) {
                return cache.get(key);
            }

            return null;
        }

        public void setImage(String key, Bitmap image) {
            cache.put(key, image);
        }

        public void clearCache() {
            cache = null;
            cache = new HashMap<>();
        }
    }
}
