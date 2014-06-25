package pl.schibsted.chat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import pl.schibsted.chat.R;
import pl.schibsted.chat.adapter.ArticlesListAdapter;
import pl.schibsted.chat.listeners.OnArticlesListener;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.model.api.HalArticleList;

import java.util.ArrayList;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticlesFragment extends Fragment {
    private ListView mArticlesListView;
    private View mEmptyView;
    private ProgressBar mProgressBar;

    private ArticlesListAdapter mArticlesAdapter;
    private OnArticlesListener mArticlesListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnArticlesListener) {
            mArticlesListener = (OnArticlesListener) activity;
        }
    }

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article, container, false);
//        mEmptyView = inflater.inflate(R.layout.view_no_results_found, null);

//        ((ViewGroup) v).addView(mEmptyView);

        mProgressBar = (ProgressBar) v.findViewById(R.id.articles_progress);

        mArticlesListView = (ListView) v.findViewById(R.id.articles_list_view);
//        mArticlesListView.setEmptyView(mEmptyView);

        mArticlesAdapter = new ArticlesListAdapter(getActivity(), new ArrayList<Article>());
        mArticlesListView.setAdapter(mArticlesAdapter);
        mArticlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mArticlesListener != null) {
                    mArticlesListener.onArticleSelected(id);
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mArticlesListener != null) {
            mArticlesListener.onArticlesShouldLoad(0, 20, mRequestListener);
        }
    }

    private final RequestListener<HalArticleList> mRequestListener = new RequestListener<HalArticleList>() {
        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(getActivity(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        @Override
        public void onRequestSuccess(HalArticleList articleList) {
            mArticlesAdapter.addItems(articleList.getEmbedded().getCollection());

            mProgressBar.animate()
                    .alpha(0.0f)
                    .setDuration(500)
                    .start();

            mArticlesListView.animate()
                    .alpha(1.0f)
                    .setDuration(700)
                    .start();
        }
    };
}
