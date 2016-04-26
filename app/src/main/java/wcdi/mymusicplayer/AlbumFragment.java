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

import wcdi.mymusicplayer.item.Album;
import wcdi.mymusicplayer.widget.AlbumAdapter;


public class AlbumFragment extends Fragment {

    private OnAlbumFragmentInteractionListener mListener;

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

        cursor.moveToFirst();

/*        if (mListView.getCount() == 0) {
            while (cursor.moveToNext())  {
                mAdapter.add(new Album(cursor));
            }
        }*/

        if (mListView.getCount() == 0) {
            do {
                mAdapter.add(new Album(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumFragmentInteractionListener) {
            mListener = (OnAlbumFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAlbumFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAlbumFragmentInteractionListener {
        void onAlbumFragmentInteraction(Album album);
        void onClickAlbumFragment();
    }
}
