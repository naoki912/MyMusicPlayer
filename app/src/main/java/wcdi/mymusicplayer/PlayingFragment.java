package wcdi.mymusicplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class PlayingFragment extends Fragment {

    public static String TAG = "PlayingFragment";

    private OnPlayingFragmentListener mListener;

    public PlayingFragment() {
        // Required empty public constructor
    }

    public static PlayingFragment newInstance() {
        return new PlayingFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.fragment_playing_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "stop1", Toast.LENGTH_SHORT);
                mListener.onClickPausePlayingFragment();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayingFragmentListener) {
            mListener = (OnPlayingFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlayingFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPlayingFragmentListener {
        void onClickPausePlayingFragment();
    }
}
