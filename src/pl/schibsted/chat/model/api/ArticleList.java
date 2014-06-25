package pl.schibsted.chat.model.api;

import com.google.gson.annotations.SerializedName;
import pl.schibsted.chat.model.Article;

import java.util.ArrayList;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticleList {
    @SerializedName("articles")
    private ArrayList<Article> collection;

    public ArticleList() {

    }

    public ArrayList<Article> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Article> collection) {
        this.collection = collection;
    }
}
