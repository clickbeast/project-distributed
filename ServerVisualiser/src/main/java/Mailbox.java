import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Mailbox {
    private int boxnumber;
    private ObservableList<Message> messageList;

    public Mailbox(int boxnumber) {
        this.boxnumber = boxnumber;
        this.messageList = FXCollections.observableArrayList();

    }

    public Mailbox(int boxnumber, List<Message> messageList) {
        this.boxnumber = boxnumber;
        this.messageList = FXCollections.observableArrayList();
    }

    public void addMessage(Message m) {
        if (messageList == null) {
            messageList = FXCollections.observableArrayList();

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


    public int getBoxnumber() {
        return boxnumber;
    }

    public void setBoxnumber(int boxnumber) {
        this.boxnumber = boxnumber;
    }

    public ObservableList<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(ObservableList<Message> messageList) {
        this.messageList = messageList;
    }

    public boolean containsMessage(Message m) {return messageList.contains(m);
    }
}
