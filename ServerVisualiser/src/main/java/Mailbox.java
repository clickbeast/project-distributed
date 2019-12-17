import java.util.ArrayList;
import java.util.List;

public class Mailbox {
    private int boxnumber;
    private List<Message> messageList=new ArrayList<>();

    public Mailbox(int boxnumber, List<Message> messageList) {
        this.boxnumber = boxnumber;
        this.messageList = messageList;
    }

}
