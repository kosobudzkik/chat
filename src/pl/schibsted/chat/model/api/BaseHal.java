package pl.schibsted.chat.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author krzysztof.kosobudzki
 */
public class BaseHal<T> {
    @SerializedName("_links")
    private Links links;
    @SerializedName("_embedded")
    private T embedded;

    public BaseHal() {

    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public T getEmbedded() {
        return embedded;
    }

    public void setEmbedded(T embedded) {
        this.embedded = embedded;
    }
}
