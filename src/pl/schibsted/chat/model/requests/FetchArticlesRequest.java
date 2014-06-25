package pl.schibsted.chat.model.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import pl.schibsted.chat.model.api.Chat;
import pl.schibsted.chat.model.api.HalArticleList;

/**
 * @author krzysztof.kosobudzki
 */
public class FetchArticlesRequest extends RetrofitSpiceRequest<HalArticleList, Chat> {
    private final int mLimit;
    private final int mOffset;

    public FetchArticlesRequest(int limit, int offset) {
        super(HalArticleList.class, Chat.class);

        mLimit = limit;
        mOffset = offset;
    }

    @Override
    public HalArticleList loadDataFromNetwork() throws Exception {
        return getService().getArticles(mLimit, mOffset);
    }
}
