import java.io.Serializable;

public class MailBoxEntry implements Serializable {
    private int boxNumber;
    private String tag;
    private String message;

    public MailBoxEntry(int boxNumber, String tag, String message) {
        this.boxNumber = boxNumber;
        this.tag = tag;
        this.message = message;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
