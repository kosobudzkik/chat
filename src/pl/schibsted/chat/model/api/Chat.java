package pl.schibsted.chat.model.api;

import pl.schibsted.chat.model.Article;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author krzysztof.kosobudzki
 */
public interface Chat {
    // ------------- ARTICLES -------------------
    // articles
    @GET("/articles")
    HalArticleList getArticles(@Query("limit") int limit, @Query("offset") int offset);

    // single article
    @GET("/articles/{id}")
    Article getArticleById(@Path("id") long articleId);
}
