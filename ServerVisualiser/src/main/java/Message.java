import java.util.Objects;

public class Message {
    private String tag;
    private String text;

    public Message(String tag, String message) {
        this.tag = tag;
        this.text = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tag='" + tag + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(tag, message.tag) &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, text);
    }
}
