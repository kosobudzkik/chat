package pl.schibsted.chat.model;

import pl.schibsted.chat.model.api.Image;

/**
 * @author krzysztof.kosobudzki
 */
public class Article {
    private long id;
    private String title;
    private String body;
    private long published;
    private Image mainImages;

    public Article() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }

    public Image getMainImages() {
        return mainImages;
    }

    public void setMainImages(Image mainImages) {
        this.mainImages = mainImages;
    }
}
