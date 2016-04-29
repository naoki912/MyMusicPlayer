package wcdi.mymusicplayer.item;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

public class Song implements Serializable {

    public String mSongId;
    public String mTrack;
    public String mTitle;
    public Long mTime; // duration
    public String mData;

    public Song(Cursor cursor) {
        mSongId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        mTrack = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
        mTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        mTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        mData = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
    }

    public static final String[] COLUMNS = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
    };
}
