package wcdi.mymusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;

import wcdi.mymusicplayer.item.Song;

public class PlayingService extends Service {

    public PlayingService() {
    }

    private NotificationManager mNotificationManager;

    private MediaPlayer mMediaPlayer;

    private ArrayList<Song> mSongArrayList;

    private Integer mPosition;

    private final Messenger mMessenger = new Messenger(new PlayingServiceHandler());

    // 命名規則を考える
    // Messages
    static final int MSG_PLAYING_PLAY_PAUSE = 0; // 再生、停止ボタン
    static final int MSG_PLAYING_START = 1; // MSG_PREPARATION_SONG_LISTの後に再生する
    static final int MSG_PREPARATION_SONG_LIST = 2; // mSongArrayListを渡す

    // Bundle
    public static final String EXTRA_SERIALIZABLE__SONG_ARRAY_LIST = "wcdi.mymusicplayer.extra.SONG_ARRAY_LIST";
    public static final String EXTRA_INT__POSITION = "wcdi.mymusicplayer.extra.POSITION";

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        startForeground();

        mMediaPlayer = new MediaPlayer();

        showNotification();
    }

    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setTicker("ticker")
                .setContentTitle("contentTitle")
                .setContentText("contentText")
                .setContentIntent(contentIntent)
                .build();

        mNotificationManager.notify(12345, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // http://developer.android.com/intl/ja/guide/components/services.html

        return START_STICKY;
    }

    /** http://developer.android.com/intl/ja/guide/components/bound-services.html#Messenger
     * 試しにHandlerの実装
     */

    private class PlayingServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PLAYING_START:

                    // ToDo 同じ曲だったら最初から再生しないようにする

                    mMediaPlayer.reset();

                    try {
                        mMediaPlayer.setDataSource(mSongArrayList.get(mPosition).mData);
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mMediaPlayer.start();
                    mMediaPlayer.isPlaying();

                    break;

                case MSG_PREPARATION_SONG_LIST:

                    // bundleを受け取る
                    // http://d.hatena.ne.jp/kumamidori/20101229/p2
                    Parcelable parcelable = (Parcelable) msg.obj;
                    Bundle bundle = (Bundle) parcelable;

                    mSongArrayList = (ArrayList<Song>) bundle.getSerializable(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST);
                    mPosition = bundle.getInt(EXTRA_INT__POSITION);

                    break;

                case MSG_PLAYING_PLAY_PAUSE:

                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    } else {
                        mMediaPlayer.start();
                    }

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
