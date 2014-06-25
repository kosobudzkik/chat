package pl.schibsted.chat.model.api;

/**
 * @author krzysztof.kosobudzki
 */
public class Link {
    private String href;

    public Link() {

    }

    public CharSequence getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
