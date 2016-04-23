package wcdi.mymusicplayer;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;


public class PlayingService extends IntentService {
    private static final String ACTION_PLAY = "wcdi.mymusicplayer.action.TEST";
    private static final String ACTION_STOP = "wcdi.mymusicplayer.action.TEST";
    private static final String ACTION_PAUSE = "wcdi.mymusicplayer.action.TEST";

    // private static final String EXTRA_PARAM1 = "wcdi.mymusicplayer.extra.PARAM1";

    public PlayingService() {
        super("PlayingService");
    }

    public static void startActionPlay(Context context) {
        Intent intent = new Intent(context, PlayingService.class);
        intent.setAction(ACTION_PLAY);
        context.startService(intent);
    }

    public static void startActionStop(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PlayingService.class);
        intent.setAction(ACTION_STOP);
        // intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public static void startActionPause(Context context) {
        Intent intent = new Intent(context, PlayingService.class);
        intent.setAction(ACTION_PAUSE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                handleActionPlay();
            } else if (ACTION_STOP.equals(action)) {
                // final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                // handleActionStop(param1);
            } else if (ACTION_PAUSE.equals(action)) {
                handleActionPause();
            }
        }
    }

    private void handleActionPlay() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionStop(String param1) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionPause(){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
