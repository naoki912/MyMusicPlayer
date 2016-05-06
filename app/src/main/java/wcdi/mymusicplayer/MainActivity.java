package wcdi.mymusicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import wcdi.mymusicplayer.item.Song;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayingFragment.OnPlayingFragmentListener,
        AlbumFragment.OnAlbumFragmentListener,
        SongFragment.OnSongFragmentListener {

//    PlayingService mPlayingService;
    Messenger mService;

    Boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
//                startActivity(PlayingActivity.createIntent(getApplicationContext()));
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(ArtistFragment.TAG)
                        .replace(R.id.fragment, ArtistFragment.newInstance())
                        .commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        setContentView(new FrameLayout(this));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, AlbumFragment.newInstance(), AlbumFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                getSupportFragmentManager().popBackStack();
            }
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        /** http://developer.android.com/intl/ja/guide/components/bound-services.html
         * アクティビティが見えている間のみサービスとやり取りする必要がある場合は、
         * onStart() の間にバインドし、onStop() の間にアンバインドします。
         * アクティビティがバックグラウンドで停止している間も応答を受け取りたい場合は、
         * onCreate() の間にバインドし、onDestroy() の間にアンバインド */
        Intent intent = new Intent(this, PlayingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            PlayingService.PlayingBinder playingBinder = (PlayingService.PlayingBinder) service;
//            mPlayingService = playingBinder.getService();
            mService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    // Serviceの停止
/*    @Override
    public void onPlayingFragmentInteractionStop() {
        // 上手く停止しないのでもう少し調べる
        if (! mBound) return;

        stopService(new Intent(MainActivity.this, PlayingService.class));
//            mPlayingService.stopSelf();
        unbindService(mConnection);
        mBound = false;
    }*/

    @Override
    public void onClickPausePlayingFragment() {
        if (! mBound) return;

        Message message = Message.obtain(null, PlayingService.MSG_PLAYING_PLAY_PAUSE);
        try {
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClickAlbumFragment(ArrayList<Song> songArrayList) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(SongFragment.TAG)
                .replace(R.id.fragment, SongFragment.newInstance(songArrayList), SongFragment.TAG)
                .commit();
    }

    @Override
    public void onSongFragmentInteraction(Song song) {

    }

    @Override
    public void onItemClickSongFragment(ArrayList<Song> songArrayList, int position) {
//        PlayingIntentService.startActionPlay(this, songArrayList, position);
//        if (mBound) {
//            Intent intent = new Intent(this, PlayingService.class);
//            startService(intent);
//            mPlayingService.start(songArrayList, position);
//        }
        if (! mBound) return;

        Bundle bundle = new Bundle();
        bundle.putSerializable(PlayingService.EXTRA_SERIALIZABLE__SONG_ARRAY_LIST, songArrayList);
        bundle.putInt(PlayingService.EXTRA_INT__POSITION, position);

        Message message_preparetion = Message.obtain(null, PlayingService.MSG_PREPARATION_SONG_LIST, bundle);
        Message message_start = Message.obtain(null, PlayingService.MSG_PLAYING_START);

        try {
            mService.send(message_preparetion);
            mService.send(message_start);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(PlayingFragment.TAG)
                .replace(R.id.fragment, PlayingFragment.newInstance(), SongFragment.TAG)
                .commit();
    }
}
