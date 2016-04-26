package wcdi.mymusicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * デフォルトで表示するFragment
 * FragmentManagerでreplaceすると下のViewが表示されてしまう
 * 解決方法としてViewの背景を設定する方法があるのだがちょっと違う気がするので、
 * このBlankなViewを1番下に表示させることにした
 * ここに画像を表示させれば壁紙にすることも可能
 */

public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}
