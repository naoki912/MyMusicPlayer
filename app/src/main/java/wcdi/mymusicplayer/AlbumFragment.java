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

import wcdi.mymusicplayer.item.Album;
import wcdi.mymusicplayer.item.Song;
import wcdi.mymusicplayer.widget.AlbumAdapter;


public class AlbumFragment extends Fragment {

    private OnAlbumFragmentListener mListener;

    private AlbumAdapter mAdapter;
    private AbsListView  mListView;

    public static String TAG = "AlbumFragment";

    public AlbumFragment() {
    }

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new AlbumAdapter(view.getContext(), R.layout.fragment_album);

        mListView = (AbsListView) view.findViewById(R.id.fragment_album_list);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * ここの処理は、Activity側で行った方がいいかも？
                 * mListener.onItemClickAlbumFragment()は、あくまで "Itemをクリックした際のListener" なので、
                 * どのItemがクリックされたという情報だけをActivityに投げて、
                 * そっち側でSongFragmentに渡すアイテムを用意したほうが良いんじゃないだろうか
                 */

                ArrayList<Song> songArrayList = new ArrayList<>();

                ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();

                Cursor cursor = contentResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        Song.COLUMNS,
                        MediaStore.Audio.Media.ALBUM_ID + "=?",
                        new String[] {
                                String.valueOf(((Album) parent.getAdapter().getItem(position)).mAlbumId)
                        },
                        null
                );

                while (cursor.moveToNext()) {
                    songArrayList.add(new Song(cursor));
                }

                cursor.close();

                mListener.onItemClickAlbumFragment(songArrayList);
            }
        });

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album.COLUMNS,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            mAdapter.addAll(new Album(cursor));
        }

        cursor.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumFragmentListener) {
            mListener = (OnAlbumFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAlbumFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // ToDo AlbumFragment, SongFragment, PlayingFragmentのonSaveInstanceStateを実装する
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public interface OnAlbumFragmentListener {
        void onItemClickAlbumFragment(ArrayList<Song> songArrayList);
    }
}
