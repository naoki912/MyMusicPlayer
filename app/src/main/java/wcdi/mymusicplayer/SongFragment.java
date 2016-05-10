package wcdi.mymusicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Comparator;

import wcdi.mymusicplayer.item.Song;
import wcdi.mymusicplayer.widget.SongAdapter;


public class SongFragment extends Fragment {

    /**
     * いずれ PlayingActivity に乗り換える
     */

    private static final String EXTRA_SERIALIZABLE__SONG_ARRAY_LIST = "songList";

    private ArrayList<Song> mSongArrayList;

    private OnSongFragmentListener mListener;

    private SongAdapter mAdapter;
    private AbsListView  mListView;

    public static String TAG = "SongFragment";

    public static String TAB_TITLE = "Songs";

    public SongFragment() {
    }

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    public static SongFragment newInstance(ArrayList<Song> songArrayList) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST, songArrayList);

        SongFragment fragment = new SongFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static SongFragment newInstance(Song ... songs) {
        SongFragment fragment = new SongFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST, songs);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mSongArrayList = (ArrayList<Song>) bundle.getSerializable(EXTRA_SERIALIZABLE__SONG_ARRAY_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new SongAdapter(view.getContext(), R.layout.fragment_song);

        mListView = (AbsListView) view.findViewById(R.id.fragment_song_list);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClickSongFragment((ArrayList<Song>) mAdapter.getAll(), position);
            }
        });

        if (mSongArrayList != null) {
            mAdapter.addAll(mSongArrayList);
            mAdapter.sort(new Comparator<Song>() {
                @Override
                public int compare(Song lhs, Song rhs) {
                    return Integer.valueOf(lhs.mTrack) - Integer.valueOf(rhs.mTrack);
                }
            });
            return;
        }

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Song.COLUMNS,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            mAdapter.add(new Song(cursor));
        }

        mAdapter.sort(new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                return Integer.valueOf(lhs.mTrack) - Integer.valueOf(rhs.mTrack);
            }
        });

        cursor.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSongFragmentListener) {
            mListener = (OnSongFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSongFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSongFragmentListener {
        void onSongFragmentInteraction(Song song);
        void onItemClickSongFragment(ArrayList<Song> songArrayList, int position);
    }
}

