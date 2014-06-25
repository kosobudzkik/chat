package pl.schibsted.chat.model.api;

/**
 * @author krzysztof.kosobudzki
 */
public class Links {
    private Link self;
    private Link next;
    private Link prev;

    public Links() {

    }

    public Link getSelf() {
        return self;
    }

    public void setSelf(Link self) {
        this.self = self;
    }

    public Link getNext() {
        return next;
    }

    public void setNext(Link next) {
        this.next = next;
    }

    public Link getPrev() {
        return prev;
    }

    public void setPrev(Link prev) {
        this.prev = prev;
    }
}
