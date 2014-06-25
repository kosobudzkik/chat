package pl.schibsted.chat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import pl.schibsted.chat.R;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticlesFragment extends Fragment {
    private ListView mArticlesListView;

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mArticlesListView = (ListView) inflater.inflate(R.layout.fragment_article, container, false);

        return mArticlesListView;
    }
}
