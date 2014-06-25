package pl.schibsted.chat.listeners;

import com.octo.android.robospice.request.listener.RequestListener;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.model.api.HalArticleList;

/**
 * @author krzysztof.kosobudzki
 */
public interface OnArticlesListener {
    void onArticleSelected(long articleId);
    void onArticleLoadSelected(long articleId, RequestListener<Article> articleRequestListener);
    void onArticlesShouldLoad(int offset, int limit, RequestListener<HalArticleList> articlesRequestListener);
}
