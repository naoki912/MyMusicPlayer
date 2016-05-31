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
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wcdi.mymusicplayer.item.Song;

public class PlayingService extends Service {

    public PlayingService() {
    }

    private NotificationManager mNotificationManager;

    private MediaPlayer mMediaPlayer;

    private List<Song> mSongArrayList;

    private List<Messenger> mClients = new ArrayList<>();

    private Integer mPosition;

    private static boolean isRunning = false;

    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());

    private static final String LOGTAG = "PlayingService";

    // Messages
    // ToDo 命名規則を考える
    // Serviceに対して送られてくるやつ
    static final int MSG_PLAYING_PLAY_PAUSE = 100; // 再生、停止ボタン
    static final int MSG_PLAYING_NEXT = 102;
    static final int MSG_PLAYING_PREVIOUS = 103;
    static final int MSG_PREPARATION_SONG_LIST = 104; // mSongArrayListを渡す

    static final int MSG_REGISTER_CLIENT = 105;
    static final int MSG_UNREGISTER_CLIENT = 106;

    // Clientに対して送るやつ
    static final int MSG_UPDATE_ALBUM_IMAGE = 200;
    static final int MSG_UPDATE_SONG_TITLE = 201;
    static final int MSG_UPDATE_ARTIST_NAME = 202;

    // ToDo 外から使わせるBundleのKeyと、Service内で使用するKeyを命名規則で分かるように分ける
    // Messages Clientに対して送ったBundleの内容のKey
    static final String EXTRA_STRING__ALBUM_ART_PATH = "wcdi.mymusicplayer.extra.ALBUM_ART_PATH";
    static final String EXTRA_STRING__SONG_TITLE = "wcdi.mymusicplayer.extra.SONG_TITLE";
    static final String EXTRA_STRING__ARTIST_NAME = "wcdi.mymusicplayer.extra.ARTIST_NAME";

    // 送られてくる方のBundleのkey
    public static final String EXTRA_SERIALIZABLE__SONG_ARRAY_LIST = "wcdi.mymusicplayer.extra.SONG_ARRAY_LIST";
    public static final String EXTRA_INT__POSITION = "wcdi.mymusicplayer.extra.POSITION";

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = new MediaPlayer();

//        showNotification();

        isRunning = true;
    }

    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, PlayingActivity.class), 0);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setTicker("ticker")
//                .setContentTitle("contentTitle")
//                .setContentText("contentText")
                .setContentIntent(contentIntent)
                .setShowWhen(false)
                .setContent(new RemoteViews(getPackageName(), R.layout.notification))
                .build();

        // ToDo どこかにServiceを止めるボタンを追加する
        startForeground(R.string.notification_id, notification);
//        mNotificationManager.notify(R.string.notification_id, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // http://developer.android.com/intl/ja/guide/components/services.html

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Serviceが終了した途端通知が消えるので、消したくない場合はここをコメントアウト
        try {
            mNotificationManager.cancel(R.string.notification_id);
        } catch (NullPointerException e) {
            Log.d(LOGTAG, "onDestroy, mNotificationManager, NullPointerException");
        }

        isRunning = false;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    // ここのmsgはMessageで使用する定数を使用する
    private void sendMessageToUI(int msg) {
        Iterator<Messenger> messengerIterator = mClients.iterator();

        // whileの中でswitchしてるけどmessengerの数少ないし多分問題ない
        while (messengerIterator.hasNext()) {
            Messenger messenger = messengerIterator.next();
            try {
                Bundle bundle;
                Message message;
                switch (msg) {
                    case MSG_UPDATE_ALBUM_IMAGE:
                        // ここはalbumIdを渡して、向こう側で非同期型でAlbumArtをセットさせる
/*                        bundle.putString(EXTRA_STRING__ALBUM_ART_PATH, mSongArrayList.get(mPosition).mTitle);

                        message = Message.obtain(null, MSG_UPDATE_ALBUM_IMAGE, bundle);
                        messenger.send(message);*/

                        break;
                    case MSG_UPDATE_SONG_TITLE:
                        bundle = new Bundle();
                        bundle.putString(EXTRA_STRING__SONG_TITLE, mSongArrayList.get(mPosition).mTitle);

                        message = Message.obtain(null, MSG_UPDATE_SONG_TITLE, bundle);
                        messenger.send(message);

                        break;
                    case  MSG_UPDATE_ARTIST_NAME:
                        bundle = new Bundle();
                        // mArtistに変更する
                        bundle.putString(EXTRA_STRING__ARTIST_NAME, mSongArrayList.get(mPosition).mTitle);

                        message = Message.obtain(null, MSG_UPDATE_ARTIST_NAME);
                        messenger.send(message);

                        break;
                }
                // メッセージの送信
            } catch (RemoteException e) {
                mClients.remove(messenger);
            }
        }
    }

    //////////////////////////////////////////
    // Nested classes
    /////////////////////////////////////////

    // http://developer.android.com/intl/ja/guide/components/bound-services.html#Messenger
    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // Clientの登録と解除
                // http://stackoverflow.com/questions/4300291/example-communication-between-activity-and-service-using-messaging
                // https://bitbucket.org/alexfu/androidserviceexample
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;

                // 以下MediaPlayerの操作
                case MSG_PREPARATION_SONG_LIST:
                    // ToDo MediaPlayerの初期化までしてしまっているのでMSG_PREPARATION_SONG_LISTを適切な名前に修正する \
                    // リストを渡す部分とMediaPlayerの初期化は上手く分ける

                    // bundleで再生リストを受け取る
                    // http://d.hatena.ne.jp/kumamidori/20101229/p2
                    Parcelable parcelable = (Parcelable) msg.obj;
                    Bundle bundle = (Bundle) parcelable;

                    mSongArrayList = (ArrayList<Song>) bundle.getSerializable(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST);
                    mPosition = bundle.getInt(EXTRA_INT__POSITION);

                    // MediaPlayer操作
                    mMediaPlayer.reset();

                    try {
                        mMediaPlayer.setDataSource(mSongArrayList.get(mPosition).mData);
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                    }

                    mMediaPlayer.start();

                    // onCreateに記述するとアプリの起動時に通知が表示されてしまうので
                    // いい方法を見つけられるまでここで実行
                    showNotification();

                    break;

                case MSG_PLAYING_PLAY_PAUSE:

                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    } else {
                        mMediaPlayer.start();
                    }
                    break;

                case MSG_PLAYING_NEXT:
                    // mSongArrayListを操作し次の曲に移動する
                    //
                    // MSG_UPDATE_ALBUM_IMAGE
                    // MSG_UPDATE_SONG_TITLE
                    // MSG_UPDATE_ARTIST_NAME
                    // 上のメッセージをClientに対して送る

                    if (mPosition < mSongArrayList.size() - 1) {
                        // ToDo ループ再生している場合はPositionを0にする

                        mPosition++;

                        mMediaPlayer.reset();

                        try {
                            mMediaPlayer.setDataSource(mSongArrayList.get(mPosition).mData);
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                        }

                        mMediaPlayer.start();

                        sendMessageToUI(MSG_UPDATE_SONG_TITLE);
                        sendMessageToUI(MSG_UPDATE_ARTIST_NAME);
                    }

                    break;

                case MSG_PLAYING_PREVIOUS:
                    // MSG_PLAYING_NEXTと同じ処理
                    // シークが1秒以上だったら曲を最初から再生する
                    // それ以外なら1つ前の曲に戻る
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
