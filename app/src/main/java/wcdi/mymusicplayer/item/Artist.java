package wcdi.mymusicplayer.item;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

public class Artist implements Serializable {

    /** 仮実装 */

    public Long mArtistId;
    public String mArtist;

    public Artist(Cursor cursor) {
        mArtistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        mArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
    }

    public static final String[] COLUMNS = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
    };
}
