package pl.schibsted.chat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import pl.schibsted.chat.R;
import pl.schibsted.chat.async.DownloadAsyncTask;
import pl.schibsted.chat.components.LatoTextView;
import pl.schibsted.chat.listeners.OnArticlesListener;
import pl.schibsted.chat.model.Article;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticleFragment extends Fragment {
    public static final String ID_KEY = "ID_KEY";

    private LatoTextView mTitleTextView;
    private ImageView mImageView;
    private WebView mWebView;

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
        View v = inflater.inflate(R.layout.fragment_single_article, container, false);

        mTitleTextView = (LatoTextView) v.findViewById(R.id.single_article_title_text_view);
        mImageView = (ImageView) v.findViewById(R.id.single_article_image_view);
        mWebView = (WebView) v.findViewById(R.id.single_article_web_view);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mArticlesListener != null) {
            mArticlesListener.onArticleLoadSelected(getArguments().getLong(ID_KEY), mRequestListener);
        }
    }

    private final RequestListener<Article> mRequestListener = new RequestListener<Article>() {
        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(getActivity(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        @Override
        public void onRequestSuccess(Article article) {
            mTitleTextView.setText(article.getTitle());
            mWebView.loadData(article.getBody(), "text/html", "utf-8");

//            SimpleLog.d(article.getMainImages().getNormalUrl());

            mTitleTextView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);

            if (article.getMainImages().getNormalUrl() != null) {
                new DownloadAsyncTask(mImageView).execute(article.getMainImages().getNormalUrl());
            }
//            mProgressBar.animate()
//                    .alpha(0.0f)
//                    .setDuration(500)
//                    .start();
//
//            mLayout.animate()
//                    .alpha(1.0f)
//                    .setDuration(700)
//                    .start();
        }
    };
}
