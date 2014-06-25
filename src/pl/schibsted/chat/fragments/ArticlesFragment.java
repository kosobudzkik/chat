package pl.schibsted.chat.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import pl.schibsted.chat.R;
import pl.schibsted.chat.model.Article;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticlesFragment extends Fragment {
    private ListView mArticlesListView;
    private ArrayAdapter<Article> mArticlesAdapter;

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mArticlesListView = (ListView) inflater.inflate(R.layout.fragment_article, container, false);
        mArticlesListView.setEmptyView(inflater.inflate(R.layout.view_no_results_found, container, false));

//        mArticlesAdapter = new ArrayAdapter<Article>()

        return mArticlesListView;
    }
}
