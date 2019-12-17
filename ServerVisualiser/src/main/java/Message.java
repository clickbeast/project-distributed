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
}
