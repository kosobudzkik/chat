package pl.schibsted.chat.model;

/**
 * @author krzysztof.kosobudzki
 */
public class Article {
    private long id;
    private String title;
    private String body;
    private int published;

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

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }
}
