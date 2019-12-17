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
}
