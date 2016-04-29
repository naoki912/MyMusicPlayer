package wcdi.mymusicplayer.item;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

public class Album implements Serializable {

    public Long mAlbumId;
    public String mAlbum;
    public String mArtist;
    public String mAlbumArt;

    public Album(Cursor cursor) {
        mAlbumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
        mAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        mArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
        mAlbumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
    }

    public static final String[] COLUMNS = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
    };
}
