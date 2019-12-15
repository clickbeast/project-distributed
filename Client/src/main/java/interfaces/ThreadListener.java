package interfaces;

import model.Conversation;
import model.Message;

public interface ThreadListener {
    public void threadFinished();

    public void newMessage(Message message, Conversation conversation);
}
