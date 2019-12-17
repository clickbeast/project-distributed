import java.util.ArrayList;
import java.util.List;

public class Mailbox {
    private int boxnumber;
    private List<Message> messageList;

    public Mailbox(int boxnumber, List<Message> messageList) {
        this.boxnumber = boxnumber;
        this.messageList = messageList;
    }
    public void addMessage(Message m){
        if(messageList==null){
            messageList=new ArrayList<>();

        }
        messageList.add(m);
    }

    @Override
    public String toString() {
        return "Mailbox{" +
                "boxnumber=" + boxnumber +
                ", messageList=" + messageList +
                '}';
    }
}
