package wcdi.mymusicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PlayingActivity extends AppCompatActivity {

    // mServiceMessengerではなくmServiceとしているコードが多いのでそっちの方がいいかも？
    private Messenger mServiceMessenger = null;

    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());

    private Boolean mIsBound;

    private static final String LOGTAG = "PlayingActivity";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, PlayingActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        findViewById(R.id.activity_playing_play_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = Message.obtain(null, PlayingService.MSG_PLAYING_PLAY_PAUSE);
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.activity_playing_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = Message.obtain(null, PlayingService.MSG_PLAYING_NEXT);
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.activity_playing_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = Message.obtain(null, PlayingService.MSG_PLAYING_PREVIOUS);
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        automaticBind();
    }

    private void automaticBind() {
        if (PlayingService.isRunning()) {
            doBindService();
        }
    }

    private void doBindService() {
        bindService(new Intent(this, PlayingService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, PlayingService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            PlayingService.PlayingBinder playingBinder = (PlayingService.PlayingBinder) service;
//            mPlayingService = playingBinder.getService();
            mServiceMessenger = new Messenger(service);
            try {
                Message message = Message.obtain(null, PlayingService.MSG_REGISTER_CLIENT);
                message.replyTo = mMessenger;
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceMessenger = null;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e(LOGTAG, "Failed to unbind from the service", t);
        }
    }

    //////////////////////////////////////////
    // Nested classes
    /////////////////////////////////////////

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PlayingService.MSG_UPDATE_ALBUM_IMAGE:
                    // アルバムアートが更新された際の処理
                    Bundle bundle = (Bundle) msg.obj;
                    String album_path = bundle.getString(PlayingService.EXTRA_STRING__ALBUM_ART_PATH);
                    // ...

                    break;

                case PlayingService.MSG_UPDATE_SONG_TITLE:
                    TextView tv = (TextView) findViewById(R.id.activity_playing_song_title);
                    Bundle b = (Bundle) msg.obj;
                    tv.setText(b.getString(PlayingService.EXTRA_STRING__SONG_TITLE));
                    break;

                case PlayingService.MSG_UPDATE_ARTIST_NAME:
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
