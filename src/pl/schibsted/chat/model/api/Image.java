package pl.schibsted.chat.model.api;

/**
 * @author krzysztof.kosobudzki
 */
public class Image {
    private String url;

    public Image() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNormalUrl() {
        return String.format(url, "fullsize");
    }
}
