package pl.schibsted.chat.model.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.model.api.Chat;

/**
 * @author krzysztof.kosobudzki
 */
public class FetchArticleRequest extends RetrofitSpiceRequest<Article, Chat> {
    private final long mArticleId;

    public FetchArticleRequest(long articleId) {
        super(Article.class, Chat.class);

        mArticleId = articleId;
    }

    @Override
    public Article loadDataFromNetwork() throws Exception {
        return getService().getArticleById(mArticleId);
    }
}
