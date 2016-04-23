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


public class PlayingFragment extends Fragment {
    // private static final String ARG_PARAM1 = "param1";

    // private String mParam1;

    public static String TAG = "PlayingFragment";

    private OnPlayingFragmentInteractionListener mListener;

    public PlayingFragment() {
        // Required empty public constructor
    }

    public static PlayingFragment newInstance() {
        PlayingFragment fragment = new PlayingFragment();

        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        */

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam = getArguments().getString(ARG_PARAM1);
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
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayingFragmentInteractionPlay();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPlayingFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayingFragmentInteractionListener) {
            mListener = (OnPlayingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlayingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPlayingFragmentInteractionListener {
        // 仮
        void onPlayingFragmentInteraction(Uri uri);
        void onPlayingFragmentInteractionPlay();
        void onPlayingFragmentInteractionStop();
    }
}
