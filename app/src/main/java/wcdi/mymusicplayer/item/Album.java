package wcdi.mymusicplayer.item;

import android.database.Cursor;
import android.provider.MediaStore;

public class Album {

    public long mAlbumId;
    public String mAlbum;
    public String mArtist;
    public String mAlbumArt;

    public Album(Cursor cursor) {
        mAlbumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
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

/*    while(corsor.moveToNext()) {
        Album album = new Album(corsor);
        adapter.add(album)
    }*/

}
