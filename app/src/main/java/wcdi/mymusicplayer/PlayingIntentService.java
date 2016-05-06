package wcdi.mymusicplayer;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

import wcdi.mymusicplayer.item.Song;


public class PlayingIntentService extends IntentService {

    /** 使用していないサービスだが、後で参考にするのでこのまま残しておく */

    private static final String ACTION_PLAY = "wcdi.mymusicplayer.action.TEST";
    private static final String ACTION_STOP = "wcdi.mymusicplayer.action.TEST";
    private static final String ACTION_PAUSE = "wcdi.mymusicplayer.action.TEST";

    private static final String EXTRA_SERIALIZABLE__SONG_ARRAY_LIST = "wcdi.mymusicplayer.extra.SONG_ARRAY_LIST";
    private static final String EXTRA_INT__POSITION = "wcdi.mymusicplayer.extra.POSITION";

    private MediaPlayer mMediaPlayer;

    public PlayingIntentService() {
        super("PlayingIntentService");
    }

    public static void startActionPlay(Context context, ArrayList<Song> songArrayList, int position) {
        Intent intent = new Intent(context, PlayingIntentService.class);
        intent.setAction(ACTION_PLAY);

        intent.putExtra(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST, songArrayList);
        intent.putExtra(EXTRA_INT__POSITION, position);

        context.startService(intent);
    }

    public static void startActionStop(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PlayingIntentService.class);
        intent.setAction(ACTION_STOP);
        // intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public static void startActionPause(Context context) {
        Intent intent = new Intent(context, PlayingIntentService.class);
        intent.setAction(ACTION_PAUSE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                final ArrayList<Song> songArrayList = (ArrayList<Song>) intent.getSerializableExtra(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST);
                final Integer position = intent.getIntExtra(EXTRA_INT__POSITION, 0);
                handleActionPlay(songArrayList, position);
            } else if (ACTION_STOP.equals(action)) {
                // final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                // handleActionStop(param1);
            } else if (ACTION_PAUSE.equals(action)) {
                handleActionPause();
            }
        }
    }

    private void handleActionPlay(ArrayList<Song> songArrayList, int position) {
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(songArrayList.get(position).mData);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.start();

    }

    private void handleActionStop(String param1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionPause(){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
