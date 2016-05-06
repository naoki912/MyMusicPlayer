package wcdi.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import wcdi.mymusicplayer.item.Artist;
import wcdi.mymusicplayer.widget.ArtistAdapter;


public class ArtistFragment extends Fragment {

    /** 仮実装 */

    private ArtistAdapter mAdapter;
    private AbsListView  mListView;

    public static String TAG = "ArtistFragment";

    public ArtistFragment() {
    }

    public static ArtistFragment newInstance() {
        return new ArtistFragment();
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
        return inflater.inflate(R.layout.fragment_artist_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ArtistAdapter(view.getContext(), R.layout.fragment_artist);

        mListView = (AbsListView) view.findViewById(R.id.fragment_artist_list);

        mListView.setAdapter(mAdapter);

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                Artist.COLUMNS,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            mAdapter.addAll(new Artist(cursor));
        }

        cursor.close();
    }
}
