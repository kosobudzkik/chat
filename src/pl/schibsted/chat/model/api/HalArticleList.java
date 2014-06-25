package pl.schibsted.chat.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author krzysztof.kosobudzki
 */
public class HalArticleList {
    @SerializedName("_links")
    private Links links;
    @SerializedName("_embedded")
    private ArticleList embedded;

    public HalArticleList() {

    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public ArticleList getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ArticleList embedded) {
        this.embedded = embedded;
    }
}
